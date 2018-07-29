package com.edu.ncu.drawlandmark;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SetProfileActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    StorageReference mStorageRef;
    private String userUID;
    private DatabaseReference mdatabase;

    Button bt_setOK;
    ImageView img_setProfilephoto;
    EditText et_setname;

    String userName;
    String useremail;

    static final int REQUEST_EXTERNAL_STORAGE = 0;  //自訂權限常數

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);

        this.bt_setOK = (Button) findViewById(R.id.bt_setOK);
        this.et_setname = (EditText) findViewById(R.id.et_setName);
        this.img_setProfilephoto = (ImageView) findViewById(R.id.img_setProfilephoto);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        userUID = user.getUid();
        useremail = user.getEmail();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        bt_setOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setProfileToDB();
            }
        });

      //  img_setProfilephoto.setOnClickListener(this);
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
        startActivity(intent);
    }

    private void initData() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    private void checkPermission(){
        int permission = ActivityCompat.checkSelfPermission(SetProfileActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限
            ActivityCompat.requestPermissions(this,
                    new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
           // getLocalImg();
        }

    }


}
