package com.edu.ncu.drawlandmark;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class SetProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    private String userUID;
    private DatabaseReference mdatabase;

    Button bt_setOK;
    EditText et_setname;

    String userName;
    String useremail;

    String refPath;
    Bundle bundle;

    //----------頭貼設定---------//
    private static final int PICKER = 100;
    private static final int REQUEST_EXTERNAL_STORAGE = 200;//自訂權限常數
    StorageReference mStorageRef;
    private String imgPath;
    TextView infoText;
    TextView tv_setProfile;
    ImageView img_setProfilephoto;
    private StorageReference riversRef;
    private ProgressBar imgUploadProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);

        initData();  //初始化firebase storage

        this.bt_setOK = (Button) findViewById(R.id.bt_setOK);
        this.et_setname = (EditText) findViewById(R.id.et_setName);
        this.img_setProfilephoto = (ImageView) findViewById(R.id.img_setProfilephoto);
        this.tv_setProfile = (TextView) findViewById(R.id.tv_setProfile);
        this.infoText = (TextView) findViewById(R.id.infotext);
        this.imgUploadProgress = (ProgressBar) findViewById(R.id.upload_progress);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        userUID = user.getUid();
        useremail = user.getEmail();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        bt_setOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // setProfileToDB();
                if(!TextUtils.isEmpty(imgPath)) {
                    imgUploadProgress.setVisibility(View.VISIBLE);
                    uploadImg(imgPath);
                    downloadImg(riversRef);
                } else{
                    Toast.makeText(SetProfileActivity.this, R.string.plz_pick_img, Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_setProfile.setOnClickListener(new View.OnClickListener() {;
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
    }

    public void setProfileToDB(){
        this.userName = et_setname.getText().toString();
        this.mdatabase = FirebaseDatabase.getInstance().getReference("drawlandmark-db");
       // DatabaseReference userRef = mdatabase.child("drawlandmark-db").child("users").child(this.userUID);
        String key = mdatabase.child("drawlandmark-db").child("users").child(this.userUID).getRef().getKey();
        DatabaseReference userRef = mdatabase.child("userProfiles");
        User user = new User(this.userUID,this.useremail, this.userName);
        userRef.push().setValue(user);
        Toast toast = Toast.makeText(SetProfileActivity.this, "你好！"+userName, Toast.LENGTH_LONG);
        //顯示Toast
        toast.show();
        Intent intent = new Intent(SetProfileActivity.this, NavigationActivity.class);
        bundle.putString("passRefPath", refPath);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initData() {
        mStorageRef = FirebaseStorage.getInstance().getReference().child("userProfilePhotos");
    }

    private void checkPermission(){
        int permission = ActivityCompat.checkSelfPermission(SetProfileActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限
            ActivityCompat.requestPermissions(this,
                    new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            getLocalImg();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocalImg();
                } else {
                    Toast toast = Toast.makeText(SetProfileActivity.this, R.string.do_nothing, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    }

    private void getLocalImg(){
        Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
        picker.setType("image/*");
        picker.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        Intent destIntent = Intent.createChooser(picker, null);
        startActivityForResult(destIntent, PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                String path = getPath(SetProfileActivity.this, uri);
                infoText.setText(path);
                Uri file = Uri.fromFile(new File(path));
                StorageReference riversRef = mStorageRef.child("userProfilePhotos").child(file.getLastPathSegment());
                UploadTask uploadTask = riversRef.putFile(file);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        infoText.setText(exception.getMessage());
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        infoText.setText("success");
                    }
                });
                imgPath = getPath(SetProfileActivity.this, uri);
                if(imgPath != null && !imgPath.equals("")) {
                    Glide.with(SetProfileActivity.this).load(imgPath).into(img_setProfilephoto);
                } else{
                    Toast.makeText(SetProfileActivity.this, R.string.load_img_fail, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private void uploadImg(String path){
        Uri file = Uri.fromFile(new File(path));
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentDisposition("universe")
                .setContentType("image/jpg")
                .build();
        riversRef = mStorageRef.child("userProfilePhotos").child(file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file, metadata);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                infoText.setText(exception.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                infoText.setText(R.string.upload_success);
                setProfileToDB();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int progress = (int)((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                imgUploadProgress.setProgress(progress);
                if(progress >= 100){
                    imgUploadProgress.setVisibility(View.GONE);
                }
            }
        });
    }

    private void downloadImg(final StorageReference ref){
        if(ref == null){
            Toast.makeText(SetProfileActivity.this, R.string.plz_pick_img, Toast.LENGTH_SHORT).show();
            return;
        }
        refPath = ref.getDownloadUrl().toString();
    }


}
