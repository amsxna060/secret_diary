package com.amansiol.magicdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.amansiol.magicdiary.adapter.NoteAdapter;
import com.amansiol.magicdiary.model.Note;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView wayForSecretDoor;
    int count=0;
    private String mUID;
    FirebaseAuth mAuth;
    FloatingActionButton addFab;
    RecyclerView notesrecycler;
    NoteAdapter noteAdapter;
    ArrayList<Note> noteslist;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(ContextCompat
                   .getColor(getApplicationContext(),
                   R.color.background));
        setContentView(R.layout.activity_main);
        checkUserLoginState();
        toolbar = findViewById(R.id.toolbar);
        addFab=findViewById(R.id.fabaddnotes);
        wayForSecretDoor = findViewById(R.id.way_for_secret_door);
        notesrecycler=findViewById(R.id.notesrecycler);
        firestore=FirebaseFirestore.getInstance();
        setSupportActionBar(toolbar);
        GridLayoutManager gridLayoutManager;
        /*
            for checking orientation of device at run time.
         */
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // code for portrait mode
            gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        } else {
            // code for landscape mode
            gridLayoutManager = new GridLayoutManager(getApplicationContext(),4);
        }
        // set the layout manager to RecyclerView
        notesrecycler.setLayoutManager(gridLayoutManager);

        noteslist = new ArrayList<>();

        getAllNotesFromCloudStore();


        wayForSecretDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count < 7) {
                    count++;
                } else if (count == 7) {
                    startActivity(new Intent(getApplicationContext(),ChatListActivity.class));
                }
            }
        });

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(), EditAndShowNotes.class);
                startActivity(intent);
            }
        });

    }

    private void getAllNotesFromCloudStore() {
        if(mUID!=null)
        {
            firestore.collection("Notes")
                    .document(mUID)
                    .collection("OneUserNotes")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            noteslist.clear();
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Note note = documentSnapshot.toObject(Note.class);
                                noteslist.add(note);
                            }
                            noteAdapter = new NoteAdapter(getApplicationContext(),noteslist);
                            notesrecycler.setAdapter(noteAdapter);
                            noteAdapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            checkUserLoginState();
        }

    }

    private void checkUserLoginState() {

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user==null||mAuth==null){
            startActivity(new Intent(MainActivity.this,LoginAndRegister.class));
            finish();
        }else {
            mUID=user.getUid();
        }
    }

    @Override
    protected void onResume() {
        checkUserLoginState();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            checkUserLoginState();
            return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        checkUserLoginState();
        getAllNotesFromCloudStore();
        super.onStart();
    }
}