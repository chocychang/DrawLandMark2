package com.edu.ncu.drawlandmark;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;
import static com.edu.ncu.drawlandmark.SetProfileActivity.getPath;
//import android.net.Uri;
//import android.database.Cursor;

public class DrawingActivity extends AppCompatActivity implements OnClickListener {

    private DrawingView drawView;
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn,undoBtn,redoBtn,insertBtn;  //添加四個按鈕:繪圖、橡皮擦、新畫紙、保存
    private float smallBrush, mediumBrush, largeBrush;

    // insert圖片進入畫布
    private static int RESULT_LOAD_IMAGE = 1;

    private static final int REQUEST_EXTERNAL_STORAGE = 200;//自訂權限常數

    String localModule;
    ImageView bt_bye;

    //-------firebase上傳照片-------//
    private StorageReference mStorageRef;
    private StorageReference riversRef;
    private static final int PICKER = 100;
    TextView infoText;
    String imgSaved;
    int porfolio_number = 2;
    //----------------------//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);
        bt_bye = (ImageView) findViewById(R.id.bt_byebye);
        drawView = findViewById(R.id.drawing);
        LinearLayout paintLayout = findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);  //獲取第一個按鈕並將其存儲
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));  //在按鈕上使用不同的可繪製圖像來顯示當前選擇的圖像

        //-----firebase------//
        mStorageRef = FirebaseStorage.getInstance().getReference().child("userPorfolio");
        //-----------//


        Bundle pic_bundle = this.getIntent().getExtras();
        localModule = pic_bundle.getString("localName");
        switch(localModule){
            case "midlakepavilion":
                drawView.setBackgroundResource(R.drawable.midlakepavilionmodule);
                break;
            case  "anpingfort":
                drawView.setBackgroundResource(R.drawable.anpingfortmodule);

                break;
            case "forbiddencity":
                drawView.setBackgroundResource(R.drawable.forbiddencitymodule);

                break;
        }

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);     //onCreate中實例化它們
        drawBtn = findViewById(R.id.draw_btn);   //在onCreate中從佈局中檢索對按鈕的引用
        drawBtn.setOnClickListener(this);  //將class設為繪圖按鈕的clickListener

        drawView.setBrushSize(mediumBrush);  //初始畫筆大小(中)

        eraseBtn = findViewById(R.id.erase_btn);  //在onCreate中檢索對該按鈕的引用
        eraseBtn.setOnClickListener(this);  //將class設為橡皮擦按鈕的clickListener

        newBtn = findViewById(R.id.new_btn);  //在onCreate中檢索對該按鈕的引用
        newBtn.setOnClickListener(this);  ///將class設為新圖紙按鈕的clickListener

        saveBtn = findViewById(R.id.save_btn);  //在onCreate中檢索對該按鈕的引用
        saveBtn.setOnClickListener(this);   ///將class設為保存按鈕的clickListener

        insertBtn = findViewById(R.id.insert_btn);
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        undoBtn = findViewById(R.id.undo_btn);
        undoBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawView.onClickUndo()) {
                    Toast nothingToast = Toast.makeText(getApplicationContext(),
                            "Nothing to Undo!", Toast.LENGTH_SHORT);
                    nothingToast.show();
                }
            }
        });

        redoBtn = findViewById(R.id.redo_btn);
        redoBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawView.onClickRedo()) {
                    Toast nothingToast = Toast.makeText(getApplicationContext(),
                            "Nothing to Redo!", Toast.LENGTH_SHORT);
                    nothingToast.show();
                }
            }
        });

        bt_bye.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DrawingActivity.this)
                        .setTitle("要離開嗎QQ")
                        .setMessage("你的畫畫還沒有儲存，確定要離開嗎？")
                        .setPositiveButton("確定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Bundle bundle = new Bundle();
                                        Intent intent2 = new Intent(DrawingActivity.this, KnowledgeActivity.class);
                                        String mapName = "";
                                        switch (localModule){
                                            case "forbiddencity":
                                                mapName = "taipei";
                                                break;
                                            case "midlakepavilion":
                                                mapName = "taichung";
                                                break;
                                            case "anpingfort":
                                                mapName = "tainan";
                                                break;
                                        }
                                        bundle.putString("passMapName",mapName);
                                        intent2.putExtras(bundle);
                                        startActivity(intent2);
                                    }
                                })
                        .setNeutralButton("不要", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

    }

    public void paintClicked(View view){
        //use chosen color 讓用戶選擇顏色
        drawView.setErase(false);  //進入繪畫，關閉橡皮擦
        if(view!=currPaint){
            //update color檢查用戶是否已點選不是當前所選顏色的顏色
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();   //檢索我們為佈局中的每個按鈕設置的標記，表示所選的顏色
            drawView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;        //更新UI以反映新選擇的顏色並將前一個顏色設置恢復正常
        }
        drawView.setBrushSize(drawView.getLastBrushSize());  //筆刷大小回復上一個，而非擦除
    }
    @Override
    public void onClick(View view){
        //respond to clicks
        if(view.getId()==R.id.draw_btn){
            //draw button clicked 檢查繪圖按鈕的點擊
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Brush size:");  //點擊按鈕時出現對話框，選擇筆刷大小
            brushDialog.setContentView(R.layout.brush_chooser);  //設置筆刷大小布局

            //聽取小的按鈕
            ImageButton smallBtn = brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            //聽取中的按鈕
            ImageButton mediumBtn = brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });

            //聽取大的按鈕
            ImageButton largeBtn = brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    drawView.setErase(false);
                    brushDialog.dismiss();
                }
            });
            //通過顯示對話框 完成onClick的繪製按鈕部分，對話框將一直顯示，直到用戶進行選擇或返回活動。
            brushDialog.show();
        }
        else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size 選擇橡皮擦按鈕
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);

            //小橡皮擦
            ImageButton smallBtn = brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });

            //中橡皮擦
            ImageButton mediumBtn = brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });

            //大橡皮擦
            ImageButton largeBtn = brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }

        else if(view.getId()==R.id.new_btn){
            //new button 開新圖紙
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("開啟新畫紙 (您將會清除現在的繪畫)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew(null);
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();
        }

        else if(view.getId()==R.id.save_btn){
            checkPermission();
            //save drawing 保存繪畫
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("完成並儲存繪畫至作品集?");
            AlertDialog.Builder builder = saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //save drawing
                    drawView.setDrawingCacheEnabled(true); //在drawingView上啟用繪圖緩存
                    imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawView.getDrawingCache(),
                            UUID.randomUUID().toString() + ".png", "drawing");
                    //將圖像寫入文件
                    if (imgSaved != null) {
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "繪畫已存至作品集!", Toast.LENGTH_SHORT);
                        savedToast.show();

                        //-------------firebase------------//

                        if(!TextUtils.isEmpty(imgSaved)) {
                            uploadImg(imgSaved);
                           // downloadImg(riversRef);
                        } else{
                            Toast.makeText(DrawingActivity.this, R.string.plz_pick_img, Toast.LENGTH_SHORT).show();
                        }


                        StorageReference riversRef = mStorageRef.child(Uri.parse(imgSaved).getLastPathSegment());

                        UploadTask uploadTask = riversRef.putFile(Uri.parse(imgSaved));
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        });

                        porfolio_number++;
                        FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
                        DatabaseReference myDatebase = fireDB.getReference("porfolio_info").child("porfolio_number");

                        //-------------firebase------------//

                        //startActivity(new Intent(DrawingActivity.this, QueMenuActivity.class));
                        Bundle bundle = new Bundle();
                        Intent intent2 = new Intent(DrawingActivity.this, KnowledgeActivity.class);
                        String mapName = "";
                        switch (localModule){
                            case "forbiddencity":
                                mapName = "taipei";
                                break;
                            case "midlakepavilion":
                                mapName = "taichung";
                                break;
                            case "anpingfort":
                                mapName = "tainan";
                                break;
                        }
                        bundle.putString("passMapName",mapName);
                        intent2.putExtras(bundle);
                        startActivity(intent2);
                    } else {
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "糟糕! 繪畫無法存取.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    //為顯示的View繪製緩存，隨機生成的UUID字符串，該方法返回創建的圖像的URL，如果操作不成功則返回null
                    drawView.destroyDrawingCache();  //銷毀圖形緩存，以便將不會重複使用現有緩存
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            saveDialog.show();
        }

    }

    private void checkPermission(){
        int permission = ActivityCompat.checkSelfPermission(DrawingActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            //未取得權限，向使用者要求允許權限
            ActivityCompat.requestPermissions(this,
                    new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast toast = Toast.makeText(DrawingActivity.this, R.string.do_nothing, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    }



    private void uploadImg(String path){
        Uri file = Uri.fromFile(new File(path));
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentDisposition("universe")
                .setContentType("image/jpg")
                .build();
        riversRef = mStorageRef.child("userPorfolio").child(file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file, metadata);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int progress = (int)((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());

                if(progress >= 100){

                }
            }
        });
    }

}

