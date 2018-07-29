package com.edu.ncu.drawlandmark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class choosedetail extends AppCompatActivity {
    private Button choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosedetail);
        choose = (Button) findViewById(R.id.choose);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //初始化Intent物件
                Intent intent = new Intent();
                //從MainActivity 到Main2Activity
                intent.setClass(choosedetail.this , DrawingActivity.class);
                //開啟Activity
                startActivity(intent);
            }
        });
    }
}