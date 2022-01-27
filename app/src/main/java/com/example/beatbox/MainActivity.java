package com.example.beatbox;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextToSpeech mtts;

    @Override
    protected void onDestroy() {
        if(mtts.isSpeaking())
            mtts.stop();
        mtts.shutdown();

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Dexter.withContext(MainActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                getStarted.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
////                        if(mtts.isSpeaking())
////                            mtts.stop();
//                        Intent intent=new Intent(MainActivity.this,Login.class);
//                        startActivity(intent);
//                    }
//                });

                //Log.d("hello","sjlfjsl");
               //mtts.shutdown();
                //finish();
                Thread thread=new Thread(){
                    public void run()
                    {
                        try{
                            mtts=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int i) {
                                    if(i==0)
                                    {
                                        mtts.setLanguage(Locale.ENGLISH);
                                        mtts.speak("Welcome To Beat Box, Enjoy Adfree Surfing of songs",TextToSpeech.QUEUE_FLUSH,null);

                                    }
                                }
                            });
                            sleep(5000);
                            //Toast.makeText(MainActivity.this, "hiiii", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(MainActivity.this,Login.class);
                            startActivity(intent);
                            finish();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };thread.start();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(MainActivity.this, "Permission is required to get started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                Toast.makeText(MainActivity.this, "Permission is required to get started", Toast.LENGTH_SHORT).show();
            }
        }).check();

    }
}