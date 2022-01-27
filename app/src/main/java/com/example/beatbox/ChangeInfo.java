package com.example.beatbox;

import static com.example.beatbox.Adapters.SongsAdapter.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.beatbox.databinding.ActivityChangeInfoBinding;
import com.example.beatbox.users.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeInfo extends AppCompatActivity {

    ActivityChangeInfoBinding b;
    FirebaseDatabase ddb;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityChangeInfoBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        ddb=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        b.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user==null)
                    user=new Users();
                user.setName(b.fullName.getText().toString());
                user.setMail(b.email.getText().toString());
                user.setPassword(b.password.getText().toString());
                user.setId(auth.getCurrentUser().getUid());
                ddb.getReference().child("Users").child(user.getId()).child("id").setValue(user.getId());
                ddb.getReference().child("Users").child(user.getId()).child("name").setValue(user.getName());
                ddb.getReference().child("Users").child(user.getId()).child("mail").setValue(user.getMail());
                ddb.getReference().child("Users").child(user.getId()).child("password").setValue(user.getPassword());
                Toast.makeText(ChangeInfo.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}