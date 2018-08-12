package com.edu.ncu.drawlandmark;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class choosedetail extends AppCompatActivity {
    private Button choose;
    String localName;

    ImageView img_picoriginal;
    ImageView img_picmodule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosedetail);
        choose = (Button) findViewById(R.id.choose);
        img_picmodule = (ImageView) findViewById(R.id.img_picmodule);
        img_picoriginal = (ImageView) findViewById(R.id.img_picorginal);

        setPicture();


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //初始化Intent物件
                Intent intent = new Intent();
                //從MainActivity 到Main2Activity
                intent.setClass(choosedetail.this , DrawingActivity.class);
                Bundle pic_bundle = new Bundle();
                pic_bundle.putString("localName",localName);
                intent.putExtras(pic_bundle);
                startActivity(intent);

                //開啟Activity
                //startActivity(intent);
            }
        });
    }

    public void setPicture(){
        Bundle bundle = this.getIntent().getExtras();
        localName = bundle.getString("passLocalName");

        switch (localName){
            case "midlakepavilion":
                img_picoriginal.setImageResource(R.drawable.midlakepavilion_2);
                img_picmodule.setImageResource(R.drawable.midlakepavilionmodule);
                break;
            case  "anpingfort":
                img_picoriginal.setImageResource(R.drawable.anpingfort_1);
                img_picmodule.setImageResource(R.drawable.anpingfortmodule);
                break;
            case "forbiddencity":
                img_picoriginal.setImageResource(R.drawable.forbiddencity_1);
                img_picmodule.setImageResource(R.drawable.forbiddencitymodule);
                break;
        }

    }

}