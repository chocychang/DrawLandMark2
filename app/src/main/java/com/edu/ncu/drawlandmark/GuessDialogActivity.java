package com.edu.ncu.drawlandmark;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class GuessDialogActivity extends Activity {

    private TextView tv_image;
    private TextView tv_text;

    public  boolean guess_status;
    String solving;
    Boolean game_finish_inguess;
    int correct_num_inguess;
    int passCoin;
    Bundle bundle;


    public GuessDialogActivity(){

    }

    public GuessDialogActivity(boolean ststus){
        this.guess_status = ststus;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_dialog);

        this.tv_image = (TextView) this.findViewById(R.id.image_dia);
        this.tv_text = (TextView) this.findViewById(R.id.text_dia);

        Intent intent = getIntent();
        this.guess_status = intent.getBooleanExtra("GuessStatus_EXTRA",TRUE);
        this.solving = intent.getStringExtra("Solve_MSG");
        this.game_finish_inguess = intent.getBooleanExtra("GAME_FINISH",FALSE);
        this.correct_num_inguess = intent.getIntExtra("CORRECT_NUM_MSG",0);
        //this.passCoin = intent.getIntExtra("GET_COIN",0);

        this.bundle = this.getIntent().getExtras();
        String UPlocalName= bundle.getString("passUPLocalName");
        bundle.putString("passUPLocalName", UPlocalName);

        setDialog();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){

            @Override
            public void run() {

                Intent intent = new Intent(GuessDialogActivity.this, SolveActivity.class);
                intent.putExtra("Solving_MSG", solving);
                intent.putExtra("GAME_FINISH",game_finish_inguess);
                intent.putExtra("CORRECT_NUM_MSG",correct_num_inguess);
                intent.putExtras(bundle);
               // intent.putExtra("GET_COIN",passCoin);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                GuessDialogActivity.this.finish();

            }}, 1000);



    }


    @Override
    public void onStart(){
        super.onStart();


        //this.guess_status=guess_status;
        //修正
        if(guess_status) {
            tv_image.setText("O");
            tv_image.setTextColor(getResources().getColor(R.color.lightGreen));
            tv_text.setText("答對了！");
            tv_text.setTextColor(getResources().getColor(R.color.lightGreen));

        } else {

            tv_image.setText("X");
            tv_image.setTextColor(getResources().getColor(R.color.lightPink));
            tv_text.setText("答錯了！");
            tv_text.setTextColor(getResources().getColor(R.color.lightPink));

        }

    }

    public void setDialog(){
        if(guess_status) {
            tv_image.setText("O");
            tv_image.setTextColor(getResources().getColor(R.color.lightGreen));
            tv_text.setText("答對了！");
            tv_text.setTextColor(getResources().getColor(R.color.lightGreen));

        } else {

            tv_image.setText("X");
            tv_image.setTextColor(getResources().getColor(R.color.lightPink));
            tv_text.setText("答錯了！");
            tv_text.setTextColor(getResources().getColor(R.color.lightPink));

        }
    }


}
