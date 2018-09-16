package com.edu.ncu.drawlandmark;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HopingActivity extends AppCompatActivity {

    private String hopingID;
    private DatabaseReference mdatabase;

    public EditText R_hoperName;
    public EditText R_hoperWord;
    public Button sending;

    public  String hoperName ;
    public  String hoperWord ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoping);

        this.R_hoperName =(EditText) findViewById(R.id.hoperName);
        this.R_hoperWord = (EditText) findViewById(R.id.hperWord);
        this.sending = (Button) findViewById(R.id.sending);

        sending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passFireBase();
            }
        });
    }

    public void passFireBase(){

        R_hoperName.setError(null);
        R_hoperWord.setError(null);

        View focusView = null;

        this.hoperName = R_hoperName.getText().toString();
        this.hoperWord = R_hoperWord.getText().toString();

        mdatabase = FirebaseDatabase.getInstance().getReference("drawlandmark-db");


        if (TextUtils.isEmpty(hoperName)) {
            R_hoperName.setError("請輸入你的名字唷");
            focusView = R_hoperName;
        } else if(TextUtils.isEmpty(hoperWord)) {
            R_hoperWord.setError("請輸入你想說的話唷");
            focusView = R_hoperWord;
        } else{
            DatabaseReference userRef = mdatabase.child("hoping");
            Hoping hoping = new Hoping(this.hopingID, this.hoperName, this.hoperWord);
            userRef.push().setValue(hoping);

            Intent intent = new Intent(HopingActivity.this, NavigationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }


    }
}
