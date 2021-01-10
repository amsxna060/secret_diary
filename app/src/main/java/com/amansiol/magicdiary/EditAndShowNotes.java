package com.amansiol.magicdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.amansiol.magicdiary.model.Color;
import com.amansiol.magicdiary.model.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;

public class EditAndShowNotes extends AppCompatActivity {
     EditText title;
     EditText description;
     Note note;
     String timestamp;
     String edit;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat
                   .getColor(getApplicationContext(),
                        R.color.background));
        setContentView(R.layout.activity_edit_and_show_notes);
        edit = getIntent().getStringExtra("edit");
        title=findViewById(R.id.heading);
        description=findViewById(R.id.description);
        firestore=FirebaseFirestore.getInstance();
        note=new Note();
    }

    void saveDataToFireStore(){

        note.setTitle(title.getText().toString());
        timestamp =String.valueOf(System.currentTimeMillis());
        note.setDescription(description.getText().toString());
        note.setTimestamp(timestamp);
        firestore.collection("Notes")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("OneUserNotes")
                .document(timestamp)
                .set(note, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"Saved...",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Something Went Wrong...",Toast.LENGTH_LONG).show();
                    }
                });
    }
    @Override
    public void onBackPressed() {
        if(edit!=null&&edit=="edit"){
            title.setText(getIntent().getStringExtra("title"));
            description.setText(getIntent().getStringExtra("description"));
            note.setTitle(title.getText().toString());
            note.setDescription(description.getText().toString());
            note.setTimestamp(getIntent().getStringExtra("timestamp"));
            firestore.collection("Notes")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("OneUserNotes")
                    .document(getIntent().getStringExtra("timestamp"))
                    .set(note, SetOptions.merge())
                    .addOnSuccessListener(unused -> Toast.makeText(getApplicationContext(),"Saved...",Toast.LENGTH_LONG).show())
                    .addOnFailureListener(e -> Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_LONG).show());
        }else {
            saveDataToFireStore();
        }
        super.onBackPressed();
    }

}