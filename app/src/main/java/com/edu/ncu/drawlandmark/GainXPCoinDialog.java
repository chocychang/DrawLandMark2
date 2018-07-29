package com.edu.ncu.drawlandmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class GainXPCoinDialog extends Activity {

    TextView tv_coinXP;
    double getXP;

    public GainXPCoinDialog(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gain_xpcoin_dialog);

        this.tv_coinXP = (TextView) findViewById(R.id.tv_coinXP);

        Intent intent = getIntent();
        this.getXP = intent.getDoubleExtra("GAINXPCOIN_MSG",100);

        this.tv_coinXP.setText("經驗值: "+this.getXP);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){

            @Override
            public void run() {
                GainXPCoinDialog.this.finish();

            }}, 3000);
    }
}
