package com.edu.ncu.drawlandmark;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class TaipeiMap extends AppCompatActivity implements View.OnClickListener{

    ImageView iv_forbiddencity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taipei_map);

        this.iv_forbiddencity = (ImageView) findViewById(R.id.iv_forbiddencity);

        this.iv_forbiddencity.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.iv_forbiddencity){
            this.startActivity( new Intent(TaipeiMap.this, PingxiActivity.class) );
        }
    }
}
