package com.edu.ncu.drawlandmark;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.LinkedList;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class QuePlayActivity extends Activity implements View.OnClickListener {

    private static int QUESTIONS = 5;

    private DBPref db;
    private TextView tv_question;
    private TextView tv_quiznum;
    private TextView tv_correctnum;
    private TextView tv_wrongnum;
    private Button option_1;
    private Button option_2;
    private Button option_3;
    private Button option_4;

    private Queue<Question> questions = new LinkedList<Question>();
    private Question active_question;
    private int question_num=0;
    int correct_num=0;
    private int wrong_num=0;

    String solve;
    private String Id;  //設定題目的id範圍的參數
    Boolean guess_status = TRUE;
    Boolean game_finish = FALSE;

    ArrayList<Integer> problem = new ArrayList<>();

    String UPlocalName;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "PlayActivity.onCreate");
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_que_play);

        this.tv_question = (TextView) this.findViewById(R.id.tv_question);
        this.tv_quiznum = (TextView) this.findViewById(R.id.quiznumber);
        this.tv_correctnum = (TextView) this.findViewById(R.id.correct_num);
        this.tv_wrongnum = (TextView) this.findViewById(R.id.wrong_num);
        this.option_1 = (Button) this.findViewById(R.id.btn_option_1);
        this.option_2 = (Button) this.findViewById(R.id.btn_option_2);
        this.option_3 = (Button) this.findViewById(R.id.btn_option_3);
        this.option_4 = (Button) this.findViewById(R.id.btn_option_4);

        this.bundle = this.getIntent().getExtras();
        UPlocalName= bundle.getString("passUPLocalName");

        this.db = new DBPref( this );

        Cursor questions = null;
        switch (UPlocalName){
            case "FORBIDDENCITY":
                questions = this.db.getQuestions(DBPref.Category.FORBIDDENCITY, DBPref.Difficulty.EASY, QUESTIONS);
                break;
            case "MIDLAKEPAVILION":
                questions = this.db.getQuestions(DBPref.Category.MIDLAKEPAVILION, DBPref.Difficulty.EASY, QUESTIONS);
                break;
            case "ANPINGFORT":
                questions = this.db.getQuestions(DBPref.Category.ANPINGFORT, DBPref.Difficulty.EASY, QUESTIONS);
                break;
        }

        if (questions.moveToFirst()) {
            do {
                String question = questions.getString( questions.getColumnIndex("question") );
                String answer   = questions.getString( questions.getColumnIndex("correct_answer") );
                Queue<String> wrong  = new LinkedList<String>();

                wrong.offer( questions.getString( questions.getColumnIndex("wrong_answer_1") ) );
                wrong.offer( questions.getString( questions.getColumnIndex("wrong_answer_2") ) );
                wrong.offer( questions.getString( questions.getColumnIndex("wrong_answer_3") ) );

                for (int i = 1; i <= 7; i++) {
                    int idx = questions.getColumnIndex("wrong_answer_" + i);
                    if (idx != -1) {
                        wrong.offer( questions.getString(idx) );
                    } else {
                        break;
                    }
                }

                String solve = questions.getString( questions.getColumnIndex("solve"));

                this.questions.offer( new Question(question, answer, (String[]) wrong.toArray(new String[wrong.size()]), solve) );
            } while(questions.moveToNext());
        }

        this.db.close();

        this.setQuestion(this.questions.poll());

        this.option_1.setOnClickListener(this);
        this.option_2.setOnClickListener(this);
        this.option_3.setOnClickListener(this);
        this.option_4.setOnClickListener(this);
    }


    private void setQuestion(Question question) {
        this.question_num++;

        this.active_question = question;

        String[] answers = question.getAnswers();

        this.tv_question.setText( question.getQuestion() );
        this.option_1.setText( answers[0] );
        this.option_2.setText( answers[1] );
        this.option_3.setText( answers[2] );
        this.option_4.setText( answers[3] );

        this.solve = question.getSolve();

        this.tv_quiznum.setText("第"+this.question_num+"題");

    }


    @Override
    public void onClick(View view) {
        Button clicked = (Button) view;
        if (clicked.getText().toString() == this.active_question.getAnswer()) {
            correct_num += 1;
            problem.add(1);
            if (this.questions.size() > 0 && question_num<5) {
                this.tv_correctnum.setText(getString(R.string.str_correctnumber, correct_num));
                this.guess_status = TRUE;
                showGuessDialog();
                this.setQuestion(this.questions.poll());
            } else {
                Toast t = Toast.makeText(this, "恭喜你完成五題！", Toast.LENGTH_LONG);
                t.show();
                showGuessDialog();
                PassGainXP();
                //this.finish();
            }
        } else {
            wrong_num += 1;
            problem.add(0);
            if (this.questions.size() > 0 && question_num<5) {
                this.tv_wrongnum.setText(getString(R.string.str_wrongnumber, wrong_num));
                this.guess_status = FALSE;
                showGuessDialog();
                this.setQuestion(this.questions.poll());
            } else {
                Toast t = Toast.makeText(this, "恭喜你完成五題！", Toast.LENGTH_LONG);
                t.show();
                showGuessDialog();
                PassGainXP();
               // this.finish();
            }
        }

       // getCoin();
    }


    public void showGuessDialog(){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, GuessDialogActivity.class);
        bundle.putString("passUPLocalName", UPlocalName);
        intent.putExtra("GuessStatus_EXTRA", this.guess_status);
        intent.putExtra("Solve_MSG", this.solve);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void PassGainXP(){
        this.game_finish = TRUE;
        Intent intent = new Intent(this, GuessDialogActivity.class);
        intent.putExtra("CORRECT_NUM_MSG", this.correct_num);
        intent.putExtra("GAME_FINISH",this.game_finish);

        startActivity(intent);
    }
/*
    public void getCoin(){
        Coin coin = new Coin();
        coin.setCorerct(correct_num);
        int earning_coin = coin.countProblemGain(problem);
        Intent intent = new Intent(this, GuessDialogActivity.class);
        intent.putExtra("GET_COIN",earning_coin);
        startActivity(intent);
    }   */
}

