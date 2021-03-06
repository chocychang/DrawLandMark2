package com.edu.ncu.drawlandmark;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TimeUpActivity extends Activity implements View.OnClickListener{

    Button btn_leave_system;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_up);

        btn_leave_system = (Button) findViewById(R.id.btn_leave_system);

        btn_leave_system.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        this.finish();
        this.onDestroy();
        System.exit(0);
    }
}
