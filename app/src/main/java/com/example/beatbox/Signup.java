package com.example.beatbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.beatbox.databinding.ActivitySignupBinding;
import com.example.beatbox.users.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

import java.io.File;
import java.util.ArrayList;

public class Signup extends AppCompatActivity {

    ActivitySignupBinding b;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        b=ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        progressDialog=new ProgressDialog(Signup.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("Your account is being created");

        b.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Signup.this,Signin.class);
                startActivity(intent);
            }
        });

        b.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(b.fullName.getText().toString().length()==0||b.email.getText().toString().length()==0||b.password.getText().toString().length()==0||b.repassword.getText().toString().length()==0)
                {
                    Toast.makeText(Signup.this, "Please Fill the required fields", Toast.LENGTH_SHORT).show();
                }
                else if(!(b.password.getText().toString().equals(b.repassword.getText().toString())))
                {
                    Toast.makeText(Signup.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressDialog.show();
                    auth.createUserWithEmailAndPassword(b.email.getText().toString(),b.password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if(task.isSuccessful())
                                    {
                                        String id=task.getResult().getUser().getUid();
                                        Users user=new Users(b.fullName.getText().toString(),b.email.getText().toString(),b.password.getText().toString(),id,new ArrayList());
                                        database.getReference().child("Users").child(id).setValue(user);
                                        //database.getReference().child("Users").child(id).setValue("Fav");
                                        database.getReference().child("Users").child(id).child("Fav");
                                        Log.d("hello","asdjflsajdflksjdfkl");
                                        Toast toast=Toast.makeText(Signup.this, "You have registered successfully", Toast.LENGTH_SHORT);
                                        toast.show();
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                toast.cancel();
                                            }
                                        }, 500);

                                        Intent intent=new Intent(Signup.this,SecondFragment.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(Signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    }
}