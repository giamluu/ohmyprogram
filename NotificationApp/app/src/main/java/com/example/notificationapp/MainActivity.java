package com.example.notificationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.notificationapp.Service.OnClearFromRecentService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PlayAble {

    private ImageButton bt_play, bt_previous, bt_next;
    private TextView title;

    NotificationManager notificationManager;
    List<Track> tracks;

    int position = 0;
    boolean isPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_previous = findViewById(R.id.bt_previous);
        bt_play = findViewById(R.id.bt_play);
        bt_next = findViewById(R.id.bt_next);
        title = findViewById(R.id.tv_title);

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

        tracks.add(new Track("Track1", "Artist1", R.drawable.anime1));
        tracks.add(new Track("Track2", "Artist2", R.drawable.anime2));
        tracks.add(new Track("Track3", "Artist3", R.drawable.anime3));
        tracks.add(new Track("Track4", "Artist4", R.drawable.anime4));
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
    }

    public void onTrackPlay() {

        CreateNotification.CreateNotification(MainActivity.this, tracks.get(position), R.drawable.ic_pause_black_24dp, position, tracks.size() - 1);
        bt_play.setImageResource(R.drawable.ic_pause_black_24dp);
        title.setText(tracks.get(position).getTitle());
        isPlay = true;

    }

    public void onTrackPause() {

        CreateNotification.CreateNotification(MainActivity.this,tracks.get(position), R.drawable.ic_play_arrow_black_24dp,position, tracks.size() - 1);
        bt_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        title.setText(tracks.get(position).getTitle());
        isPlay = false;
    }

    @Override
    public void onTrackNext() {

        position++;
        CreateNotification.CreateNotification(MainActivity.this,tracks.get(position), R.drawable.ic_pause_black_24dp, position, tracks.size() - 1);
        title.setText(tracks.get(position).getTitle());
        bt_play.setImageResource(R.drawable.ic_pause_black_24dp);
        isPlay = true;

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
