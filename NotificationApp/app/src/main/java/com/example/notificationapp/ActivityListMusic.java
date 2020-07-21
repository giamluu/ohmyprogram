package com.example.notificationapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ActivityListMusic extends AppCompatActivity {

    private ListView musicList;
    ArrayList<Track> arrayMusicList;
    MusicAdapter musicAdapter;
    MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_music);

        init();

        musicAdapter = new MusicAdapter(this,R.layout.layout_line_music, mainActivity.tracks);
        musicList.setAdapter(musicAdapter);

        musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ActivityListMusic.this, MainActivity.class);
                intent.putExtra("position", position);
                mainActivity.isSend = true;
                startActivity(intent);
            }
        });
    }

    private void init() {
        musicList = findViewById(R.id.listMusic);
        arrayMusicList = new ArrayList<>();
    }
}
