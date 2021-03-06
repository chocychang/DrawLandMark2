package com.edu.ncu.drawlandmark;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , View.OnClickListener{

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    StorageReference mStorageRef;
    private String userUID="";
    String useremail;
    TextView tv_taipei;
    TextView tv_taichung;
    TextView tv_username;
    TextView tv_keelung;
    TextView tv_newtaipei;
    TextView tv_taoyuan;
    TextView tv_hsinchu;
    TextView tv_miaoli;
    TextView tv_changhua;
    TextView tv_nantou;
    TextView tv_yunlin;
    TextView tv_chiayi;
    TextView tv_tainan;
    TextView tv_kaohsiung;
    TextView tv_pingtung;
    TextView tv_hualien;
    TextView tv_yilan;
    TextView tv_taitung;

    ImageView userpicture;
    ImageView bubble_anim;
  //  AnimationDrawable animationDrawable;


    private DatabaseReference mdatabase;

    //Bundle bundle;
    //String refPath;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initData();
        //setUserPhoto();

        //---------找UID-------------//
        auth = FirebaseAuth.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        //userUID = user.getUid();
        //useremail = user.getEmail();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(
                    @NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null) {
                    Log.d("onAuthStateChanged", "登入:" +
                            user.getUid());
                    userUID = user.getUid();
                    useremail = user.getEmail();
                }else{
                    Log.d("onAuthStateChanged", "已登出");
                    //startActivity(new Intent(NavigationActivity.this, StartOptionActivity.class));
                }
            }
        };
        //---------找UID-------------//

        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.tv_username = (TextView) findViewById(R.id.tv_nameinN);
        this.userpicture = (ImageView) findViewById(R.id.userpicture);

        //-------動畫設置------------------//
        this.bubble_anim = (ImageView) findViewById(R.id.bubble_anim);

        bubble_anim.setImageResource(R.drawable.bubble_anim);
        AnimationDrawable animationDrawable = (AnimationDrawable) bubble_anim.getDrawable();
        animationDrawable.start();
        //-------動畫設置------------------//

        this.tv_taipei = (TextView) findViewById(R.id.taipei);
        this.tv_taichung = (TextView) findViewById(R.id.taichung);
        this.tv_newtaipei = (TextView) findViewById(R.id.newtaipei);
        this.tv_taoyuan = (TextView) findViewById(R.id.taoyuan);
        this.tv_hsinchu = (TextView) findViewById(R.id.hsinchu);
        this.tv_miaoli = (TextView) findViewById(R.id.miaoli);
        this.tv_changhua = (TextView) findViewById(R.id.changhua);
        this.tv_nantou = (TextView) findViewById(R.id.nantou);
        this.tv_yunlin = (TextView) findViewById(R.id.yunlin);
        this.tv_chiayi = (TextView) findViewById(R.id.chiayi);
        this.tv_tainan = (TextView) findViewById(R.id.tainan);
        this.tv_kaohsiung = (TextView) findViewById(R.id.kaohsiung);
        this.tv_pingtung = (TextView) findViewById(R.id.pingtung);
        this.tv_hualien = (TextView) findViewById(R.id.hualien);
        this.tv_yilan = (TextView) findViewById(R.id.yilan);
        this.tv_taitung = (TextView) findViewById(R.id.taitung);
        this.tv_keelung = (TextView) findViewById(R.id.keelung);

        this.tv_taipei.setOnClickListener(this);
        this.tv_taichung.setOnClickListener(this);
        this.tv_newtaipei.setOnClickListener(this);
        this.tv_taoyuan.setOnClickListener(this);
        this.tv_hsinchu.setOnClickListener(this);
        this.tv_miaoli.setOnClickListener(this);
        this.tv_changhua.setOnClickListener(this);
        this.tv_nantou.setOnClickListener(this);
        this.tv_yunlin.setOnClickListener(this);
        this.tv_chiayi.setOnClickListener(this);
        this.tv_tainan.setOnClickListener(this);
        this.tv_kaohsiung.setOnClickListener(this);
        this.tv_pingtung.setOnClickListener(this);
        this.tv_hualien.setOnClickListener(this);
        this.tv_yilan.setOnClickListener(this);
        this.tv_taitung.setOnClickListener(this);
        this.tv_keelung.setOnClickListener(this);

        Typeface type = Typeface.createFromAsset(getAssets(), "setofont.ttf");

        this.tv_taipei.setTypeface(type);
        this.tv_taichung.setTypeface(type);
        this.tv_newtaipei.setTypeface(type);
        this.tv_taoyuan.setTypeface(type);
        this.tv_hsinchu.setTypeface(type);
        this.tv_miaoli.setTypeface(type);
        this.tv_changhua.setTypeface(type);
        this.tv_nantou.setTypeface(type);
        this.tv_yunlin.setTypeface(type);
        this.tv_chiayi.setTypeface(type);
        this.tv_tainan.setTypeface(type);
        this.tv_kaohsiung.setTypeface(type);
        this.tv_pingtung.setTypeface(type);
        this.tv_hualien.setTypeface(type);
        this.tv_yilan.setTypeface(type);
        this.tv_taitung.setTypeface(type);
        this.tv_keelung.setTypeface(type);


        //-------timer設置------------------//
        Intent intent = new Intent(NavigationActivity.this, TimeService.class);
        startService(intent);
        //----------------------------------//

        this.mdatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mdatabase.child("users");

        RetrieveUserProfileData();

        //--------設定大頭貼-----//
        //this.bundle = this.getIntent().getExtras();
       // refPath= bundle.getString("passRefPath");

        SharedPreferences pref = getSharedPreferences("first",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("status",false);
        editor.commit();

    }
/*
    private void initData() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void setUserPhoto(){
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(refPath);
        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(NavigationActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(mStorageRef)
                        .into(userpicture);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }   */

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.taipei){
            setCityName("taipei");
        }else if(view.getId() == R.id.taichung){
            setCityName("taichung");
        }else if(view.getId() == R.id.tainan){
            setCityName("tainan");
        }else{
            new AlertDialog.Builder(NavigationActivity.this)
                    .setTitle("近請期待")
                    .setMessage("目前開放台北、台中、台南三地，之後會陸續增加！")
                    .setPositiveButton("我知道了",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })

                    .show();
        }
    }

    public void setCityName(String cityname){
        String cityName = cityname;
        Bundle bundle = new Bundle();
        Intent intent = new Intent(NavigationActivity.this, TaipeiMap.class);
        bundle.putString("passCityName",cityName);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    //在firebse中取得使用者名稱
    public void RetrieveUserProfileData(){
        DatabaseReference databaseReference = mdatabase.child("drawlandmark-db").child("userProfiles").child("-LIVha0o6LJt_uUKXwiA").child("useremail");
        Query queryRef = databaseReference.equalTo(useremail);
        Log.d("測試",""+useremail);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    Log.d("測試",""+user.useremail);
                }

               // String name = dataSnapshot.child("username").getValue().toString();
                //tv_username.setText(user.username);

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        };
        queryRef.addValueEventListener(postListener);
    }
    public static void RetrieveDateValueEvent(String columnName,
                                              ValueEventListener valueEventListener) {
        if(columnName != null) {
            FirebaseDatabase mFireBaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = mFireBaseDatabase.getReference(columnName);
            databaseReference.addValueEventListener(valueEventListener);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void showNotice(){
        new AlertDialog.Builder(NavigationActivity.this)
                .setTitle("近請期待")
                .setMessage("本功能暫未開放！非常抱歉QQ")
                .setPositiveButton("我知道了",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })

                .show();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_maccount) {
            // Handle the camera action
            showNotice();
        } else if (id == R.id.nav_gallery) {
            showNotice();
        } else if (id == R.id.nav_manage) {
            //this.startActivity( new Intent(NavigationActivity.this, SetupActivity.class) );
            showNotice();

        } else if (id == R.id.nav_mFB) {
            this.startActivity( new Intent(NavigationActivity.this, HopingActivity.class));
        } else if (id == R.id.nav_score) {
            showNotice();
        } else if (id == R.id.log_out){
            this.auth.signOut();
            Intent intent = new Intent(NavigationActivity.this, StartOptionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            this.startActivity( intent );
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
