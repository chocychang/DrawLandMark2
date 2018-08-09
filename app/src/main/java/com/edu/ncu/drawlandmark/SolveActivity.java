package com.edu.ncu.drawlandmark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class SolveActivity extends Activity implements View.OnClickListener{

    TextView tv_solve;
    Button bt_leave;
    Button bt_nextQuestion;
    Boolean game_finish;
    TextView tv_finishgame;
    int getCorrect_num;
    int gainCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solve);

        tv_solve = (TextView) findViewById(R.id.tv_solve);
        bt_leave = (Button) findViewById(R.id.bt_leave);
        bt_nextQuestion = (Button) findViewById(R.id.bt_nextquestion);
        tv_finishgame = (TextView) findViewById(R.id.tv_finishgame);

        Intent intent  = getIntent();
        this.tv_solve.setText(intent.getStringExtra("Solving_MSG"));
        this.game_finish = intent.getBooleanExtra("GAME_FINISH",FALSE);
        this.getCorrect_num = intent.getIntExtra("CORRECT_NUM_MSG",0);
        this.gainCoin = intent.getIntExtra("GET_COIN",0);


        bt_leave.setOnClickListener(this);
        bt_nextQuestion.setOnClickListener(this);

        if(this.game_finish == TRUE){
            bt_nextQuestion.setVisibility(View.INVISIBLE);
            bt_nextQuestion.setClickable(FALSE);
            tv_finishgame.setVisibility(View.VISIBLE);
        }

        Log.d("測試gamefinish",""+this.tv_solve);
    }

    @Override
    public void onClick(View view){
        Button clicked = (Button) view;
        if(clicked == this.bt_leave){
            Intent intent2 = new Intent(SolveActivity.this, ForbiddencityActivity.class);
            startActivity(intent2);
            if(this.game_finish){
                showGainXP();
            }
        } else if(clicked == this.bt_nextQuestion){
            this.finish();
        }
    }

    public void showGainXP(){
        Level updatelevel = new Level();
        double XP = updatelevel.getgainXP(this.getCorrect_num);
        Intent intent = new Intent(this, GainXPCoinDialog.class);
        intent.putExtra("GAINXPCOIN_MSG", XP);
        intent.putExtra("gainCoin",gainCoin);
        startActivity(intent);
    }
}
