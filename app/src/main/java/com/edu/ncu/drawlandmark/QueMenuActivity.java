
package com.edu.ncu.drawlandmark;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class QueMenuActivity extends AppCompatActivity implements View.OnClickListener{

    private Button playButton;
    private Button statsButton;
    private Button settingsButton;
    String UPlocalName;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_que_menu);

        this.playButton = (Button) this.findViewById(R.id.btn_play);
        this.statsButton = (Button) this.findViewById(R.id.btn_stats);
        this.settingsButton = (Button) this.findViewById(R.id.btn_settings);

        this.playButton.setOnClickListener(this);
        this.statsButton.setOnClickListener((View.OnClickListener) this);
        this.settingsButton.setOnClickListener((View.OnClickListener) this);

        this.bundle = this.getIntent().getExtras();
        UPlocalName= bundle.getString("passUPLocalName");
        bundle.putString("passUPLocalName", UPlocalName);
    }

    //@Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_play) {
            Intent intent = new Intent(QueMenuActivity.this, QuePlayActivity.class);
            intent.putExtras(this.bundle);
            startActivity(intent);
        } else if (view.getId() == R.id.btn_stats) {

        } else if (view.getId() == R.id.btn_settings) {

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
