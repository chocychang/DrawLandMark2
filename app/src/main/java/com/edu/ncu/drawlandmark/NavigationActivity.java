package com.edu.ncu.drawlandmark;

import android.content.Intent;
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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , View.OnClickListener{

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    private String userUID="";
    String useremail;
    TextView tv_taipei;
    TextView tv_username;

    private DatabaseReference mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    startActivity(new Intent(NavigationActivity.this, StartOptionActivity.class));
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

        this.tv_taipei = (TextView) findViewById(R.id.taipei);
        this.tv_username = (TextView) findViewById(R.id.tv_nameinN);

        this.tv_taipei.setOnClickListener(this);

        //-------timer設置------------------//
        Intent intent = new Intent(NavigationActivity.this, TimeService.class);
        startService(intent);
        //----------------------------------//

        this.mdatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mdatabase.child("users");

        RetrieveUserProfileData();

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.taipei){
            this.startActivity( new Intent(NavigationActivity.this, TaipeiMap.class) );
        }
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_maccount) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_manage) {
            this.startActivity( new Intent(NavigationActivity.this, SetupActivity.class) );

        } else if (id == R.id.nav_mFB) {

        } else if (id == R.id.nav_score) {

        } else if (id == R.id.log_out){
            this.auth.signOut();
            this.startActivity( new Intent(NavigationActivity.this, StartOptionActivity.class) );
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
