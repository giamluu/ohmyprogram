package com.example.readingsong;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class AudioPlayer extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btPlay, btPrevious, btNext;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private Runnable runnable;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        btPlay = findViewById(R.id.bt_play);
        btNext = findViewById(R.id.bt_next);
        btPrevious = findViewById(R.id.bt_previous);
        seekBar = findViewById(R.id.seekBar2);
//        handler = new Handler() {
//            @Override
//            public void publish(LogRecord record) {
//
//            }
//
//            @Override
//            public void flush() {
//
//            }
//
//            @Override
//            public void close() throws SecurityException {
//
//            }
//        };

        btPrevious.setOnClickListener(this);
        btPlay.setOnClickListener(this);
        btNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

    }
}
