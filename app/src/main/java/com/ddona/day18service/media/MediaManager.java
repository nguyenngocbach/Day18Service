package com.ddona.day18service.media;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.MediaStore;

import com.ddona.day18service.model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaManager {
    private List<Song> mSongs;
    private MediaPlayer mPlayer;
    private int currentSong;
    private int currentStatus;
    private final int STATUS_IDEAL = 1;
    private final int STATUS_PLAYING = 2;
    private final int STATUS_PAUSED = 3;
    private final int STATUS_STOP = 4;


    private Context mContext;

    public MediaManager(Context mContext) {
        this.mContext = mContext;
        currentStatus = STATUS_IDEAL;
        getAllAudioFilesExternal();
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayer.start();
                currentStatus = STATUS_PLAYING;
            }
        });
    }

    public void playSong() {
        mPlayer.reset();
        try {
            mPlayer.setDataSource(mSongs.get(currentSong).getPath());
            mPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pauseSong() {
        mPlayer.pause();
        currentStatus = STATUS_PAUSED;
    }

    public void stop() {
        //mPlayer.stop();
        mPlayer.reset();
        mPlayer.release();
    }

    public MediaPlayer getPlayer() {
        return mPlayer;
    }

    public void seekTo(int position) {
        mPlayer.seekTo(position);
    }

    public Song getCurrentSong() {
        return mSongs.get(currentSong);
    }

    public void nextSong() {
        if (currentSong >= mSongs.size() - 1) {
            currentSong = 0;
        } else {
            currentSong++;
        }
        playSong();
    }

    public void previousSong() {
        if (currentSong <= 0) {
            currentSong = mSongs.size() - 1;
        } else {
            currentSong--;
        }
        playSong();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void getAllAudioFilesExternal() {
        String columnsName[] = new String[]{MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.DISPLAY_NAME,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.TITLE};
        Cursor c = mContext.getContentResolver().
                query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, columnsName, null, null, null, null);
        c.moveToFirst();
        mSongs = new ArrayList<>();
        int pathIndex = c.getColumnIndex(columnsName[0]);
        int fullName = c.getColumnIndex(columnsName[1]);
        int durationIndex = c.getColumnIndex(columnsName[2]);
        int authorIndex = c.getColumnIndex(columnsName[3]);
        int songNameIndex = c.getColumnIndex(columnsName[4]);

        while (c.isAfterLast() == false) {
            Song item = new Song(c.getString(songNameIndex),
                    c.getString(pathIndex),
                    c.getString(durationIndex),
                    c.getString(authorIndex),
                    c.getString(fullName));
            mSongs.add(item);
            c.moveToNext();
        }
        c.close();
    }
}
