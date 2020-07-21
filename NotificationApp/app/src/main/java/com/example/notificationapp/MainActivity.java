package com.example.notificationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimationUtilsCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notificationapp.Service.OnClearFromRecentService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PlayAble {

    private ImageButton bt_play, bt_previous, bt_next, activityList, randomMusic, loopMusic;
    private TextView title, currentTimeMusic, musicTime;
    private ImageView pictureMusic;
    private SeekBar seekMusic;


    private Animation animation;
    private RandomMusic isRandomMusic;

    static MediaPlayer mediaPlayer = new MediaPlayer();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

    NotificationManager notificationManager;
    public static List<Track> tracks;
    private final int UNLOOP = 0, LOOP = 1, LOOPONE = 2;

    int position = 0;
    boolean isPlay = false, isRandom = false;
    static int currentTime = 0, isLoopMusic = 0;
    public static boolean isSend = false, isTouchSeekBar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_previous      = findViewById(R.id.bt_previous);
        bt_play          = findViewById(R.id.bt_play);
        bt_next          = findViewById(R.id.bt_next);
        title            = findViewById(R.id.tv_title);
        currentTimeMusic = findViewById(R.id.currentTimeMusic);
        musicTime        = findViewById(R.id.timeMusic);
        seekMusic        = findViewById(R.id.seekPlay);
        pictureMusic     = findViewById(R.id.pictureMusic);
        activityList     = findViewById(R.id.activityList);
        randomMusic      = findViewById(R.id.randomMusic);
        loopMusic        = findViewById(R.id.loopMusic);

        animation = AnimationUtils.loadAnimation(this, R.anim.rotateanim);

        populateTrack();

        isRandomMusic = new RandomMusic(tracks.size());
        Log.i("giam",tracks.size()+"  size list music");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();

            registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"));
            startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        }


        if (isSend) {
            currentTime = 0;
            Intent intent = getIntent();
            position = intent.getIntExtra("position", position);
            onTrackPlay();
            isSend = false;
        }

        //set seekBar time
        seekMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentTimeMusic.setText(simpleDateFormat.format(seekMusic.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTouchSeekBar = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currentTime = seekMusic.getProgress();
                mediaPlayer.seekTo(seekMusic.getProgress());
                isTouchSeekBar = false;
            }
        });

        //button Random
        randomMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRandom) {
                    randomMusic.setBackgroundResource(R.drawable.unrandom);
                    isRandom = false;
                } else {
                    randomMusic.setBackgroundResource(R.drawable.random);
                    isRandom = true;
                }
            }
        });

        //Loop button
        loopMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoopMusic == UNLOOP) {
                    isLoopMusic = LOOP;
                    loopMusic.setBackgroundResource(R.drawable.loop);
                    //this is loop
                } else if (isLoopMusic == LOOP) {
                    isLoopMusic = LOOPONE;
                    loopMusic.setBackgroundResource(R.drawable.loopone);
                    //this is loop one
                } else if (isLoopMusic == LOOPONE) {
                    isLoopMusic = UNLOOP;
                    loopMusic.setBackgroundResource(R.drawable.unloop);
                    //this is unloop
                }
            }
        });

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

        //activity list music
        activityList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityListMusic.class);
                startActivity(intent);
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

        if (position > 0 || isRandom) {
            RotateAnimation(16000);
            if (isRandom) {
                position = isRandomMusic.init(tracks.size());
            } else {
                position--;
            }
            CreateNotification.CreateNotification(MainActivity.this, tracks.get(position), R.drawable.ic_pause_black_24dp, position, tracks.size() - 1);
            title.setText(tracks.get(position).getTitle());
            bt_play.setImageResource(R.drawable.ic_pause_black_24dp);
            isPlay = true;
            currentTime = 0;
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(MainActivity.this, tracks.get(position).getUrlMusic());
            setTimeDuration();
            mediaPlayer.start();
            updateTimeSong();
            pictureMusic.setImageResource(tracks.get(position).getImage());
        } else {
            Toast.makeText(this, "Không có bài trước.",Toast.LENGTH_SHORT).show();
        }
    }

    public void onTrackPlay() {

        RotateAnimation(16000);
        mediaPlayer.release();

        CreateNotification.CreateNotification(MainActivity.this, tracks.get(position), R.drawable.ic_pause_black_24dp, position, tracks.size() - 1);
        bt_play.setImageResource(R.drawable.ic_pause_black_24dp);
        title.setText(tracks.get(position).getTitle());
        isPlay = true;

        mediaPlayer = MediaPlayer.create(MainActivity.this, tracks.get(position).getUrlMusic());

        setTimeDuration();
        pictureMusic.setImageResource(tracks.get(position).getImage());

        mediaPlayer.seekTo(currentTime);
        mediaPlayer.start();
        updateTimeSong();
    }

    public void onTrackPause() {

        RotateAnimation(0);

        CreateNotification.CreateNotification(MainActivity.this,tracks.get(position), R.drawable.ic_play_arrow_black_24dp,position, tracks.size() - 1);
        bt_play.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        title.setText(tracks.get(position).getTitle());
        isPlay = false;
        mediaPlayer.pause();
        currentTime = mediaPlayer.getCurrentPosition();
    }

    @Override
    public void onTrackNext() {

        if (position < tracks.size() - 1 || isRandom) {
            RotateAnimation(16000);
            if (isRandom) {
                position = isRandomMusic.init(tracks.size());
            } else
                position++;
            CreateNotification.CreateNotification(MainActivity.this, tracks.get(position), R.drawable.ic_pause_black_24dp, position, tracks.size() - 1);
            title.setText(tracks.get(position).getTitle());
            bt_play.setImageResource(R.drawable.ic_pause_black_24dp);
            isPlay = true;
            mediaPlayer.release();
            currentTime = 0;
            mediaPlayer = MediaPlayer.create(MainActivity.this, tracks.get(position).getUrlMusic());
            setTimeDuration();
            mediaPlayer.start();
            updateTimeSong();
            pictureMusic.setImageResource(tracks.get(position).getImage());
        } else {
            Toast.makeText(this, "Không có bài hát tiếp theo",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.cancelAll();
        }
        unregisterReceiver(broadcastReceiver);
    }

    private void setTimeDuration() {
        //Định dạng time
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        musicTime.setText(simpleDateFormat.format(mediaPlayer.getDuration()));

        //set max of seekBar
        seekMusic.setMax(mediaPlayer.getDuration());
    }
    private void updateTimeSong() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isTouchSeekBar) {
                    currentTimeMusic.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                    //update seekProgress
                    seekMusic.setProgress(mediaPlayer.getCurrentPosition());

                    //kiểm tra bài hát kết thúc

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if (position >= tracks.size() - 1) {
                                onTrackPlay();
                                onTrackPause();
                            } else
                                onTrackNext();
                        }
                    });
                }
            handler.postDelayed(this, 500);
            }
        }, 100);
    }
    private void RotateAnimation(int speed) {
        animation.setDuration(speed);
        animation.setRepeatCount(1);
        animation.setZAdjustment(-1);
        pictureMusic.startAnimation(animation);
    }
}
