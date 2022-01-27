package com.example.beatbox;

import static com.example.beatbox.Adapters.SongsAdapter.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.beatbox.Adapters.SongsAdapter;
import com.example.beatbox.databinding.ActivityFavouritesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Favourites extends AppCompatActivity {

    ActivityFavouritesBinding b;
    FirebaseAuth auth;
    FirebaseDatabase database;
    public static ArrayList<File> songsa;
    public static String[] gunne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityFavouritesBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        songsa=user.getFav();
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        int n;
        if (songsa == null)
            n = 0;
        else
            n = songsa.size();
        String s;
        if(n==1)
            s="There is 1 song";
        else
            s = "There are " + n + " songs";
        // System.out.println(s);
        Toast toast=Toast.makeText(Favourites.this,s, Toast.LENGTH_SHORT);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 1000);
        gunne=new String[n];
        gunne = new String[n];
        for (int i = 0; i < gunne.length; i++) {
            gunne[i] = songsa.get(i).getName().replace(".mp3", "");
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(Favourites.this, android.R.layout.simple_list_item_1,gunne);
        b.listView.setAdapter(adapter);

        b.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(Favourites.this,PlaySong.class);
                intent.putExtra("position",i);
                intent.putExtra("boolean",true);
                startActivity(intent);
            }
        });
    }
}