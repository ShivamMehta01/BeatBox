package com.example.beatbox;

import static com.example.beatbox.Favourites.gunne;
import static com.example.beatbox.Favourites.songsa;
import static com.example.beatbox.SecondFragment.gaane;
import static com.example.beatbox.SecondFragment.songs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity implements SensorEventListener,MediaPlayer.OnCompletionListener {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        finished=true;
    }
    TextView secret;
    TextView textView;
    ImageView play,left,right;
    SeekBar seekBar;
    TextView currTime;
    TextView finalTime;
    int pos;
    String songName;
    ArrayList<File> songList;
    MediaPlayer mediaPlayer;
    Thread updateSeek;
    Handler handler;
    boolean finished;
    boolean favourites;

    // For shake gestures
    private SensorManager sensorManager;
    private Sensor accelerometer;
    boolean shaky;
    private float x=0,y=0,z=0,lx=0,ly=0,lz=0;
    private float dx,dy,dz;
    private float th=5f;
    boolean isStart=true;
    boolean able=false;
    boolean clickness=true;

    // For autoplay
    ImageView repeat;

    @Override
    protected void onPause() {
        super.onPause();
        if(shaky)
            sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(shaky)
            sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_play_song);
        secret=findViewById(R.id.secret);
        textView=findViewById(R.id.textView2);
        play=findViewById(R.id.imageView15);
        left=findViewById(R.id.imageView14);
        right=findViewById(R.id.imageView16);
        seekBar=findViewById(R.id.seekBar);
        repeat=findViewById(R.id.autoplay);
        favourites=false;
        Intent intent=getIntent();
        pos=intent.getIntExtra("position",0);
        favourites=intent.getExtras().getBoolean("boolean");

        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null)
        {
            accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            shaky=true;
        }
        else
        {
            shaky=false;
        }

        if(favourites)
        {
            songName=gunne[pos];
            songList=songsa;
        }
        else
        {
            songName=gaane[pos];
            songList=songs;
        }
        textView.setText(songName);
        textView.setSelected(true);
        Uri uri=Uri.parse(songList.get(pos).toString());
        mediaPlayer=MediaPlayer.create(this,uri);
        mediaPlayer.start();
        //mediaPlayer.setLooping(true);
        currTime=findViewById(R.id.currTime);
        finalTime=findViewById(R.id.finalTime);
        finalTime.setText(createTime(mediaPlayer.getDuration()));
        handler=new Handler();
        int delay=1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(finished)
                    return;
                currTime.setText(createTime(mediaPlayer.getCurrentPosition()));
                handler.postDelayed(this,delay);
            }
        },delay);
        secret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickness)
                {
                    if(shaky)
                    {
                        secret.setText("You can shake your device to play/pause");
                        able=true;
                        Toast.makeText(PlaySong.this, "You can disable this feature by clicking that text again", Toast.LENGTH_SHORT).show();
                    }
                    else
                        secret.setText("Sorry, shake gesture is currently unavailable");
                    clickness=false;
                }
                else
                {
                    secret.setText("Disabled the shake gesture");
                    able=false;
                    Toast.makeText(PlaySong.this, "You can activate this feature by clicking that text again", Toast.LENGTH_SHORT).show();
                    clickness=true;
                }
            }
        });
        seekBar.setMax(mediaPlayer.getDuration());
        finalTime=findViewById(R.id.finalTime);
        finalTime.setTextColor(Color.BLACK);
        currTime=findViewById(R.id.currTime);
        currTime.setTextColor(Color.BLACK);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        setSeekBar();
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(repeat.getTag().equals("off"))
                {
                    repeat.setImageResource(R.drawable.rr1);
                    repeat.setTag("on");
                }
                else
                {
                    repeat.setImageResource(R.drawable.rrr);
                    repeat.setTag("off");
                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying())
                {
                    play.setImageResource(R.drawable.pl);
                    mediaPlayer.pause();
                }
                else
                {
                    play.setImageResource(R.drawable.d3);
                    mediaPlayer.start();
                }
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftClicked();
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightClicked();
            }
        });
        mediaPlayer.setOnCompletionListener(this);
    }
    public String createTime(int t)
    {
        String time="";
        int min=t/1000/60;
        int sec=t/1000%60;
        time+=min+":";
        if(sec<10)
            time+="0";
        time+=sec;
        return time;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        x=sensorEvent.values[0];
        y=sensorEvent.values[1];
        z=sensorEvent.values[2];

        dx=Math.abs(x-lx);
        dy=Math.abs(y-ly);
        dz=Math.abs(z-lz);

        if(((dx>th&&dy>th)||(dy>th&&dz>th)||(dz>th&&dx>th))&&(!isStart)&&(able))
        {
            if(mediaPlayer.isPlaying())
            {
                play.setImageResource(R.drawable.pl);
                mediaPlayer.pause();
                Log.d("yyyyyyy","zzzzzzz");
            }
            else
            {
                play.setImageResource(R.drawable.d3);
                mediaPlayer.start();
            }
        }

        lx=x;
        ly=y;
        lz=z;
        isStart=false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void rightClicked()
    {
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if(pos==songList.size()-1)
        {
            pos=0;
        }
        else
        {
            pos=pos+1;
        }
        seekBar.setProgress(0);
        textView.setText(songList.get(pos).getName().replace(".mp3",""));
        textView.setSelected(true);
        play.setImageResource(R.drawable.d3);
        Uri uri=Uri.parse(songList.get(pos).toString());
        mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        //mediaPlayer.setLooping(true);
        seekBar.setMax(mediaPlayer.getDuration());
        setSeekBar();
        finalTime.setText(createTime(mediaPlayer.getDuration()));
        mediaPlayer.setOnCompletionListener(this);
    }

    public void leftClicked()
    {
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if(pos==0)
        {
            pos=songList.size()-1;
        }
        else
        {
            pos=pos-1;
        }
        seekBar.setProgress(0);
        textView.setText(songList.get(pos).getName().replace(".mp3",""));
        textView.setSelected(true);
        play.setImageResource(R.drawable.d3);
        Uri uri=Uri.parse(songList.get(pos).toString());
        mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        //mediaPlayer.setLooping(true);
        seekBar.setMax(mediaPlayer.getDuration());
        setSeekBar();
        finalTime.setText(createTime(mediaPlayer.getDuration()));
        mediaPlayer.setOnCompletionListener(this);
    }

    public void setSeekBar()
    {
        updateSeek=new Thread(){
            @Override
            public void run() {
                int currPos=0;
                try{
                    while(currPos<mediaPlayer.getDuration())
                    {
                        currPos=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currPos);
                        sleep(500);
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };updateSeek.start();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(repeat.getTag().equals("off"))
            rightClicked();
        else
        {
            pos-=1;
            rightClicked();
        }
        mediaPlayer.setOnCompletionListener(this);
    }
}