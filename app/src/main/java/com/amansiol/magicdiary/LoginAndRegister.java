package com.amansiol.magicdiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class LoginAndRegister extends AppCompatActivity {
    Button loginRegisterBtn;
    EditText Register_pass;
    EditText Register_email;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    void init()
    {
        loginRegisterBtn =findViewById(R.id.login_btn);
        Register_email=findViewById(R.id.login_email);
        Register_pass=findViewById(R.id.login_pass);
        progressBar=findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(ContextCompat
                .getColor(getApplicationContext(),
                        R.color.background));
        setContentView(R.layout.activity_login_and_register);
        init();
        loginRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEmail=Register_email.getText().toString().trim();
                String sPass=Register_pass.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(sEmail).matches()){
                    Register_email.setError("incorrect Email");
                    Register_email.setFocusable(true);
                }else
                if(sPass.length()<6)
                {
                    Register_pass.setError("Password must be more than 6 character");
                    Register_pass.setFocusable(true);
                }else {

                    performLoginOrAccountCreation(sEmail,sPass);
                }
            }
        });
    }

    private void registerUser(String sEmail, String sPass) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(sEmail,sPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressBar.setVisibility(View.GONE);
                            Intent intent=new Intent(LoginAndRegister.this,BasicActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
//                            Animatoo.animateSplit(RegisterActivity.this);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginAndRegister.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
    private void LoginUser(String sEmail, String sPass) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(sEmail, sPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressBar.setVisibility(View.GONE);
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent=new Intent(LoginAndRegister.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
//                            Animatoo.animateSplit(LoginActivity.this);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginAndRegister.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginAndRegister.this, ""+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void performLoginOrAccountCreation(final String email, final String password){
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(this, new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<SignInMethodQueryResult> task) {
                if(task.isSuccessful()){
                    SignInMethodQueryResult result= task.getResult();
                    if(result != null && result.getSignInMethods()!=null && result.getSignInMethods().size() > 0){

                        LoginUser(email,password);

                    }else {
                        registerUser(email,password);
                        Toast.makeText(getApplicationContext(),"registering...",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(LoginAndRegister.this, "There is a problem, please try again later.",Toast.LENGTH_SHORT).show();
                }
                }
        });

    }

}