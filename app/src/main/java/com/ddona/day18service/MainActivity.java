package com.ddona.day18service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ddona.day18service.model.Song;
import com.ddona.day18service.service.MusicService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnNext, btnPause, btnPlay, btnPrevious;
    private MusicService mMusicService;
    private TextView tvSongName, tvDuration;
    private SeekBar sbMusic;
    private Handler mHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateUI();
            mHandler.postDelayed(this, 300);
        }
    };

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            mMusicService = binder.getMusicService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        mHandler.postDelayed(runnable, 300);
    }

    private void updateUI() {
        if (mMusicService != null) {
            Song song = mMusicService.getMediaManager().getCurrentSong();
            String totalTime = getDuration(song.getDuration());
            int currentPosition = mMusicService.getMediaManager().getPlayer().getCurrentPosition();
            String currentTime = getDuration(currentPosition + "");
            tvDuration.setText(currentTime + "/" + totalTime);
            tvSongName.setText(song.getName());
            sbMusic.setMax(Integer.parseInt(song.getDuration()));
            sbMusic.setProgress(currentPosition);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    private void initView() {
        btnNext = findViewById(R.id.btn_next);
        btnPause = findViewById(R.id.btn_pause);
        btnPlay = findViewById(R.id.btn_play);
        btnPrevious = findViewById(R.id.btn_previous);
        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        tvSongName = findViewById(R.id.tv_song_name);
        tvDuration = findViewById(R.id.tv_duration);
        sbMusic = findViewById(R.id.sb_music);
        sbMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (mMusicService != null) {
                        mMusicService.getMediaManager().seekTo(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (mMusicService != null) {
                    mMusicService.getMediaManager().nextSong();
                }
                break;
            case R.id.btn_pause:
                if (mMusicService != null) {
                    mMusicService.getMediaManager().pauseSong();
                }
                break;
            case R.id.btn_play:
                if (mMusicService != null) {
                    mMusicService.getMediaManager().playSong();
                    Song song = mMusicService.getMediaManager().getCurrentSong();
                    tvSongName.setText(song.getName());
                    tvDuration.setText(getDuration(song.getDuration()));
                }
                break;
            case R.id.btn_previous:
                if (mMusicService != null) {
                    mMusicService.getMediaManager().previousSong();
                }
                break;
        }
    }

    private String getDuration(String time) {
        long duration = Long.parseLong(time);
        int minutes = (int) (duration / 1000 / 60);
        int seconds = (int) ((duration / 1000) % 60);
        return minutes + " : " + seconds;
    }
}
