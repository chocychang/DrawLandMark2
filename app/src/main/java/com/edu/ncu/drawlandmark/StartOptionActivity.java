package com.edu.ncu.drawlandmark;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartOptionActivity extends Activity implements View.OnClickListener {

    Button bt_register;
    Button bt_sign_in;
    Button bt_guestlogin;

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_option);

        bt_register = (Button) findViewById(R.id.W_bt_register);
        bt_sign_in = (Button) findViewById(R.id.W_bt_signi_in);
        bt_guestlogin = (Button) findViewById(R.id.bt_guestLogin);

        bt_sign_in.setOnClickListener(this);
        bt_register.setOnClickListener(this);
        bt_guestlogin.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null) {
                    Log.d("onAuthStateChanged", "登入:"+
                            user.getUid());
                    userUID =  user.getUid();
                    startActivity(new Intent(StartOptionActivity.this, NavigationActivity.class));
                }else{
                    Log.d("onAuthStateChanged", "已登出或未登入");
                }
            }
        };
    }

    @Override
    public void onClick(View view){
        if(view.getId() == R.id.W_bt_register){
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            //this.finish();
        }else if(view.getId() == R.id.bt_guestLogin){
            Intent intent = new Intent(this, GuideActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
