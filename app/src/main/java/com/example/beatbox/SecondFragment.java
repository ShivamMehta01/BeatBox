package com.example.beatbox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beatbox.Adapters.RecyclerViewInterface;
import com.example.beatbox.Adapters.SongsAdapter;
import com.example.beatbox.databinding.ActivitySecondFragmentBinding;
import com.example.beatbox.users.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

public class SecondFragment extends AppCompatActivity implements RecyclerViewInterface {

    ActivitySecondFragmentBinding b;
    FirebaseAuth auth;
    FirebaseDatabase database;
    public static ArrayList<File> songs;
    static String[] gaane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        b=ActivitySecondFragmentBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        auth=FirebaseAuth.getInstance();
        songs = fetchSongs(Environment.getExternalStorageDirectory().getAbsoluteFile());
        database=FirebaseDatabase.getInstance();


        int n;
        if (songs == null)
            n = 0;
        else
            n = songs.size();
        String s;
        if(n==1)
            s="There is 1 song";
        else
            s = "There are " + n + " songs";
        // System.out.println(s);
        Handler h=new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SecondFragment.this, s, Toast.LENGTH_SHORT).show();
            }
        },1000);
        gaane = new String[n];
        for (int i = 0; i < gaane.length; i++) {
            gaane[i] = songs.get(i).getName().replace(".mp3", "");
        }



//        ArrayAdapter adapter = new ArrayAdapter<String>(SecondFragment.this, android.R.layout.simple_list_item_1, gaane) {
//            @NonNull
//            @Override
//            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                TextView text = (TextView) view.findViewById(android.R.id.text1);
//                text.setTextColor(Color.BLACK);
//                return view;
//            }
//        };

        SongsAdapter adapter=new SongsAdapter(gaane,SecondFragment.this,this);
        b.recyclerView.setAdapter(adapter);

        b.recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        b.meraListView0.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(SecondFragment.this, PlaySong.class);
//                String currentSong = b.meraListView0.getItemAtPosition(i).toString();
//                intent.putExtra("currentSong", currentSong);
//                intent.putExtra("position", i);
//                intent.putExtra("songList", songs);
//                startActivity(intent);
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.Logout:
            {
                auth.signOut();
                Toast.makeText(SecondFragment.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(SecondFragment.this,Login.class);
                startActivity(intent);
                break;
            }
            case R.id.Favorites:
            {
                Intent intent=new Intent(SecondFragment.this,Favourites.class);
                startActivity(intent);
                break;
            }
            case R.id.ChangeInfo:
            {
                Intent intent=new Intent(SecondFragment.this,ChangeInfo.class);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public static ArrayList<File> fetchSongs(File fp) {
        ArrayList<File> a = new ArrayList<>();
        File[] file = fp.listFiles();
        if(file==null)
        {
            //System.out.println("asdjflasjdfksjd");
            return a;
        }
        for (File f : file) {
            // System.out.println("helllllllllllllllllllllllll");
            try{
                if (f.isDirectory() && !f.isHidden())
                    a.addAll(fetchSongs(f));
                else if (f.getName().endsWith(".mp3") && !f.getName().startsWith("."))
                {
                    //System.out.println("shivammmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
                    a.add(f);
                }
            }
            catch (Exception e)
            {

            }
        }
        return a;
    }

    @Override
    public void onItemClickedddddddd(int position) {
//        Intent intent = new Intent(SecondFragment.this, PlaySong.class);
//        //Toast.makeText(this, "item clicked", Toast.LENGTH_SHORT).show();
//        String currentSong = gaane[position];
//        intent.putExtra("currentSong", currentSong);
//        intent.putExtra("position", position);
//        intent.putExtra("songList", songs);
//        startActivity(intent);
    }
}