package com.example.notificationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.notificationapp.Service.OnClearFromRecentService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PlayAble {

    private ImageButton bt_play, bt_previous, bt_next;
    private TextView title;
    private ListView Musics;

    MediaPlayer mediaPlayer;

    NotificationManager notificationManager;
    List<Track> tracks;

    int position = 0;
    boolean isPlay = false;
    int currentTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_previous = findViewById(R.id.bt_previous);
        bt_play = findViewById(R.id.bt_play);
        bt_next = findViewById(R.id.bt_next);
        title = findViewById(R.id.tv_title);
        Musics = findViewById(R.id.ListMusic);

        populateTrack();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();

            registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"));
            startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        }

        bt_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTrackPrevious();
            }
        });

        bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlay) {
                    onTrackPause();
                } else
                    onTrackPlay();
            }
        });

        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTrackNext();
            }
        });
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "KOD DEV", NotificationManager.IMPORTANCE_LOW);

            notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    //populate list with tracks
    private void populateTrack() {
        tracks = new ArrayList<>();

        tracks.add(new Track("Cánh hồng phai", "Artist1", R.drawable.anime1, R.raw.canhhongphai));
        tracks.add(new Track("Cho anh say đắm", "Artist2", R.drawable.anime2, R.raw.choanhsay));
        tracks.add(new Track("Có chắc đây là yêu", "Artist3", R.drawable.anime3, R.raw.cochacyeuladay));
        tracks.add(new Track("Em ơi lên phố", "Artist4", R.drawable.anime4, R.raw.emoilenpho));
        tracks.add(new Track("Nếu ta ngược lối", "Artist4", R.drawable.anime4, R.raw.neutanguocloi));

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionName");

            switch (action) {
                case CreateNotification.ACTION_PREVIOUS:
                    onTrackPrevious();
                    break;
                case CreateNotification.ACTION_PLAY:
                    if (isPlay) {
                        onTrackPause();
                    }else
                        onTrackPlay();
                    break;
                case CreateNotification.ACTION_NEXT:
                    onTrackNext();
                    break;
            }
        }
    };

    public void onTrackPrevious() {

        position--;
        CreateNotification.CreateNotification(MainActivity.this, tracks.get(position), R.drawable.ic_pause_black_24dp, position, tracks.size() - 1);
        title.setText(tracks.get(position).getTitle());
        bt_play.setImageResource(R.drawable.ic_pause_black_24dp);
        isPlay = true;
        currentTime = 0;
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(MainActivity.this, tracks.get(position).getUrlMusic());
        mediaPlayer.start();
    }

    public void onTrackPlay() {

        CreateNotification.CreateNotification(MainActivity.this, tracks.get(position), R.drawable.ic_pause_black_24dp, position, tracks.size() - 1);
        bt_play.setImageResource(R.drawable.ic_pause_black_24dp);
        title.setText(tracks.get(position).getTitle());
        isPlay = true;

        mediaPlayer = MediaPlayer.create(MainActivity.this, tracks.get(position).getUrlMusic());
        if (currentTime == 0)
            mediaPlayer.start();
        else {
            mediaPlayer.seekTo(currentTime);
            mediaPlayer.start();
        }


    }

    public void onTrackPause() {

        CreateNotification.CreateNotification(MainActivity.this,tracks.get(position), R.drawable.ic_play_arrow_black_24dp,position, tracks.size() - 1);
        bt_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        title.setText(tracks.get(position).getTitle());
        isPlay = false;
        mediaPlayer.pause();
        currentTime = mediaPlayer.getCurrentPosition();
        Log.i("giam.luu",mediaPlayer.getCurrentPosition()+"");
    }

    @Override
    public void onTrackNext() {

        position++;
        CreateNotification.CreateNotification(MainActivity.this,tracks.get(position), R.drawable.ic_pause_black_24dp, position, tracks.size() - 1);
        title.setText(tracks.get(position).getTitle());
        bt_play.setImageResource(R.drawable.ic_pause_black_24dp);
        isPlay = true;
        mediaPlayer.release();
        currentTime = 0;
        mediaPlayer = MediaPlayer.create(MainActivity.this, tracks.get(position).getUrlMusic());
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.cancelAll();
        }
        unregisterReceiver(broadcastReceiver);
    }
}
