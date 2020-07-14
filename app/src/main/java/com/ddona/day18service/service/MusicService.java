package com.ddona.day18service.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.ddona.day18service.R;
import com.ddona.day18service.media.MediaManager;

public class MusicService extends Service {
    private MediaManager mMediaManager;
    private MusicBinder binder = new MusicBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MusicBinder extends Binder {
        public MusicService getMusicService() {
            return MusicService.this;
        }
    }

    @Override

    public void onCreate() {
        super.onCreate();
        mMediaManager = new MediaManager(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mMediaManager.playSong();
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Music");
        builder.setContentText("I'm playing music");
        builder.setSmallIcon(R.mipmap.ic_launcher);

        NotificationChannel channel =
                new NotificationChannel("music", "music",
                        NotificationManager.IMPORTANCE_HIGH);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        builder.setChannelId(channel.getId());

        Notification notification = builder.build();
        startForeground(1, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public MediaManager getMediaManager() {
        return mMediaManager;
    }
}
