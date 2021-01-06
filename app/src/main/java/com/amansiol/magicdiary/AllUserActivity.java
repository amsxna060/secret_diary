package com.amansiol.magicdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.amansiol.magicdiary.adapter.UserAdapter;
import com.amansiol.magicdiary.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllUserActivity extends AppCompatActivity {

    RecyclerView allusers_recycler;
    FirebaseDatabase mfirebasedatabase;
    DatabaseReference mref;
    FirebaseUser muser;
    FirebaseAuth firebaseAuth;
    UserAdapter allusers_adapter;
    List<User> Userlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);
        allusers_recycler=findViewById(R.id.allusers_recycler);
        allusers_recycler.setLayoutManager(new LinearLayoutManager(this));
        allusers_recycler.setHasFixedSize(true);
        mfirebasedatabase=FirebaseDatabase.getInstance();
        mref=mfirebasedatabase.getReference("Users");
        firebaseAuth=FirebaseAuth.getInstance();
        muser=firebaseAuth.getCurrentUser();
        Userlist=new ArrayList<>();
        getAllUser();
    }

    private void getAllUser() {
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userlist.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    User allusers_models=ds.getValue(User.class);
                    if(!allusers_models.getUID().equals(muser.getUid())){

                        Userlist.add(allusers_models);
                    }
                    allusers_adapter=new UserAdapter(AllUserActivity.this,Userlist);
                    allusers_recycler.setAdapter(allusers_adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}