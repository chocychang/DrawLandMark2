package com.edu.ncu.drawlandmark;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PorfolioActivity extends AppCompatActivity {

    ImageView iv_porforlio;
    ImageView test;

    StorageReference mStorageRef;
    FirebaseDatabase fireDB;
    DatabaseReference myDatebase;

    String number;

    private GridView gridView;
    private ArrayList<ImageView> image = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_porfolio);

        this.iv_porforlio = (ImageView) findViewById(R.id.iv_porforlio);

/*
        fireDB = FirebaseDatabase.getInstance();
        myDatebase = fireDB.getReference("porfolio_info").child("porfolio_number");
        myDatebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                number = dataSnapshot.getValue(String.class);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
            }
        });  */

        mStorageRef = FirebaseStorage.getInstance().getReference().child("userPorfolio").child("182");
        mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(PorfolioActivity.this)
                        .using(new FirebaseImageLoader())
                        .load(mStorageRef)
                        .into(iv_porforlio);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });

        this.image.add(iv_porforlio);

        List<Map<String, Object>> items = new ArrayList<>();
        for (int i = 0; i < image.size(); i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("image", image.get(i));
            items.add(item);
        }
        final SimpleAdapter adapter = new SimpleAdapter(this,
                items, R.layout.porfolio_item, new String[]{"image"},
                new int[]{R.id.image});
        gridView = (GridView)findViewById(R.id.gv_porfolio);
        gridView.setNumColumns(3);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });



    }
}
