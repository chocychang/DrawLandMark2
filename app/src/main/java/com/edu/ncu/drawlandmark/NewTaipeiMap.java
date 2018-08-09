package com.edu.ncu.drawlandmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class NewTaipeiMap extends Activity implements View.OnClickListener{

    ImageView img_pingxi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_taipei_map);

        this.img_pingxi = (ImageView) findViewById(R.id.img_pingxi);

        this.img_pingxi.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.img_pingxi){
            this.startActivity( new Intent(NewTaipeiMap.this, ForbiddencityActivity.class) );
        }
    }
}
