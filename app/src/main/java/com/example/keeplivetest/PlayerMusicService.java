package com.example.keeplivetest;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class PlayerMusicService extends Service {
    private final static String TAG = "PlayerMusicService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, TAG + "---->onCreate,启动服务");
        MediaPlayerManager.getInstance(this.getApplicationContext()).init();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startPlayMusic();
            }
        }).start();
        return START_STICKY;
    }

    private void startPlayMusic() {
        MediaPlayerManager.getInstance(this.getApplicationContext()).startMusic();
    }

    private void stopPlayMusic() {
        MediaPlayerManager.getInstance(this.getApplicationContext()).stopMusic();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayMusic();
        Log.d(TAG, TAG + "---->onCreate,停止服务");
        // 重启自己
        Intent intent = new Intent(getApplicationContext(), PlayerMusicService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }else {
            startService(intent);
        }
    }
}
