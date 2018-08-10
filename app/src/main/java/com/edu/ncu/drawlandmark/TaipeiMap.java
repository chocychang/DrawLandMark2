package com.edu.ncu.drawlandmark;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TaipeiMap extends AppCompatActivity implements View.OnClickListener{

    ImageView iv_building;
    ImageView iv_citymap;
    TextView tv_cityname;
    TextView tv_buildingName;
    String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taipei_map);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        this.iv_building = (ImageView) findViewById(R.id.iv_building);
        this.iv_citymap = (ImageView) findViewById(R.id.img_citymap);
        this.tv_cityname = (TextView) findViewById(R.id.tv_cityname);
        this.tv_buildingName = (TextView) findViewById(R.id.tv_buildingName);

        final Bundle bundle = this.getIntent().getExtras();
        cityName = bundle.getString("passCityName");
        setMapView(cityName);

        this.iv_building.setOnClickListener(this);


    }

    public void setMapView(String cityname){
        switch (cityname){
            case "taipei":
                this.iv_citymap.setImageResource(R.drawable.taipeicity);
                this.iv_building.setImageResource(R.drawable.forbiddencity);
                this.tv_buildingName.setText("故宮");
                this.tv_cityname.setText("台北市");
                break;
            case "taichung":
                this.iv_citymap.setImageResource(R.drawable.taichungcity);
                this.iv_building.setImageResource(R.drawable.pavilion);
                this.tv_buildingName.setText("湖心亭");
                this.tv_cityname.setText("台中市");
                break;
            case "tainan":
                this.iv_citymap.setImageResource(R.drawable.tainancity);
                this.iv_building.setImageResource(R.drawable.anping_fort);
                this.tv_buildingName.setText("安平古堡");
                this.tv_cityname.setText("台南市");
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (cityName){
            case "taipei":
                this.startActivity( new Intent(TaipeiMap.this, ForbiddencityActivity.class) );
                break;
            case "taichung":
                this.startActivity( new Intent(TaipeiMap.this, MidLakePavilionActivity.class) );
                break;
            case "tainan":
                this.startActivity( new Intent(TaipeiMap.this, AnpingFortActivity.class) );
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
