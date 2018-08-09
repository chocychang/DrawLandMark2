package com.edu.ncu.drawlandmark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ChooseDraw extends AppCompatActivity {

    private Button go;
    String localName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_draw);
        go = (Button) findViewById(R.id.choosepic);

        final Bundle bundle = this.getIntent().getExtras();
        localName = bundle.getString("passLocalName");
        bundle.putString("passLocalName",localName);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //初始化Intent物件
                Intent intent = new Intent();
                //從MainActivity 到Main2Activity
                intent.setClass(ChooseDraw.this , choosedetail.class);
                intent.putExtras(bundle);
                //開啟Activity
                startActivity(intent);
            }
        });

    }
}
