package com.example.beatbox.Adapters;

import static com.example.beatbox.SecondFragment.songs;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beatbox.PlaySong;
import com.example.beatbox.R;
import com.example.beatbox.SecondFragment;
import com.example.beatbox.users.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.viewHolder> {

    String[] myList;
    Context context;
    RecyclerViewInterface interfac;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference ref;
    public static Users user;
    int cnt=-1;

    public SongsAdapter(String[] myList, Context context,RecyclerViewInterface interfac) {
        this.myList = myList;
        this.context = context;
        this.interfac=interfac;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.songs_list_interface,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.name.setText(myList[position]);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(holder.getAdapterPosition()>=0)
                {
                    if(user.getFav().contains(songs.get(holder.getAdapterPosition())))
                    {
                        holder.star.setImageResource(R.drawable.starfilled);
                        holder.star.setTag("filled");
                        Log.d("iiiii","hello");
                        cnt++;
                    }
                    Log.d("wwwww","holder");
                }
                handler.postDelayed(this,100);
            }
        },100);
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,PlaySong.class);
                //String currentSong = holder.name.getText().toString();
                //intent.putExtra("currentSong", currentSong);
                intent.putExtra("position", holder.getAdapterPosition());
                //intent.putExtra("songList", myList);
                intent.putExtra("boolean",false);
                context.startActivity(intent);
            }
        });
        holder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.star.getTag().toString().equals("unfilled"))
                {
                    holder.star.setImageResource(R.drawable.starfilled);
                    holder.star.setTag("filled");
                    auth=FirebaseAuth.getInstance();
                    user.getFav().add(songs.get(holder.getAdapterPosition()));
                    database=FirebaseDatabase.getInstance();
                    database.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("Fav").child(String.valueOf(holder.getAdapterPosition())).setValue(songs.get(holder.getAdapterPosition()).toString());
                    Toast toast=Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 500);
                }
                else
                {
                    holder.star.setImageResource(R.drawable.starunfilled);
                    holder.star.setTag("unfilled");
                    user.getFav().remove((songs.get((holder.getAdapterPosition()))));
                    database=FirebaseDatabase.getInstance();
                    database.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("Fav").child(String.valueOf(holder.getAdapterPosition())).setValue(null);
                    Toast toast=Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 500);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList.length;
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView star;
        TextView name;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            star=itemView.findViewById(R.id.starUnfilled);
            name=itemView.findViewById(R.id.name);
            name.setSelected(true);
            auth=FirebaseAuth.getInstance();
            database=FirebaseDatabase.getInstance();
            ref=database.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("Fav");
            user=new Users();

            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.exists())
                    {
                        //Log.d("wwwwwww","dsgdsfgdf");
                        File f=new File(snapshot.getValue().toString());
                        if(!user.getFav().contains(f))
                        {
                            Log.d("wwwww","file");
                            user.getFav().add(f);
                        }
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
