package com.edu.ncu.drawlandmark;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.edu.ncu.drawlandmark.R;
import com.edu.ncu.drawlandmark.TimeService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tv_newtaipei;
    Button bt_signOut;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    private String userUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tv_newtaipei = (TextView) findViewById(R.id.tv_newtaipei);
        this.bt_signOut = (Button) findViewById(R.id.bt_sign_out);

        this.tv_newtaipei.setOnClickListener(this);
        this.bt_signOut.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(
                    @NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user==null) {
                    startActivity(new Intent(MainActivity.this, StartOptionActivity.class));
                    Log.d("onAuthStateChanged", "已登出或未登入");
                }else{
                    Log.d("onAuthStateChanged", "登入:"+
                            user.getUid());
                    userUID =  user.getUid();
                }
            }
        };

        //-------timer設置------------------//

        Intent intent = new Intent(MainActivity.this, TimeService.class);
        startService(intent);

        //----------------------------------//

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.tv_newtaipei){
            this.startActivity( new Intent(MainActivity.this, NavigationActivity.class) );
        } else if(view.getId() == R.id.bt_sign_out){
            this.auth.signOut();
            this.startActivity( new Intent(MainActivity.this, StartOptionActivity.class) );
        }
    }


}
