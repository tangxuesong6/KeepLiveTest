package com.example.keeplivetest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class DaemonService extends Service {
    private static final String TAG = "DaemonService";
    public static final int NOTICE_ID = 100;
    private ScreenReceiverUtil mScreenReceiverUtil;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "DaemonService---->onCreate被调用，启动前台service");
        Toast.makeText(this, "启动Deamon", Toast.LENGTH_SHORT).show();
        //如果API大于18，需要弹出一个可见通知
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelName = "通知消息";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel("channelId", channelName, importance);
                NotificationManager notificationManager = (NotificationManager) this.getSystemService(
                        NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
                Notification.Builder builder = new Notification.Builder(this);
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setContentTitle("KeepAppAlive");
                builder.setContentText("DaemonService is runing...");
                builder.setChannelId("channelId");
                startForeground(NOTICE_ID, builder.build());
                Intent intent = new Intent(this, CancelNoticeService.class);
                startService(intent);
            } else {
                Notification.Builder builder = new Notification.Builder(this);
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setContentTitle("KeepAppAlive");
                builder.setContentText("DaemonService is runing...");
                startForeground(NOTICE_ID, builder.build());
                // 如果觉得常驻通知栏体验不好
                // 可以通过启动CancelNoticeService，将通知移除，oom_adj值不变
                Intent intent = new Intent(this, CancelNoticeService.class);
                startService(intent);
            }

        } else {
            startForeground(NOTICE_ID, new Notification());
        }
        mScreenReceiverUtil = new ScreenReceiverUtil(this);
        mScreenReceiverUtil.setScreenReceiverListener(new ScreenReceiverUtil.SreenStateListener() {
            @Override
            public void onSreenOn() {
                stopPlayMusicService();
            }

            @Override
            public void onSreenOff() {
                startMusic();
            }

            @Override
            public void onUserPresent() {

            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 如果Service被终止
        // 当资源允许情况下，重启service
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 如果Service被杀死，干掉通知
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            NotificationManager mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mManager.cancel(NOTICE_ID);
        }
        Log.d(TAG, "DaemonService---->onDestroy，前台service被杀死");
        // 重启自己
        Intent intent = new Intent(getApplicationContext(), DaemonService.class);
        startService(intent);
    }

    private void startMusic() {
        Intent intent = new Intent(this, PlayerMusicService.class);
        startService(intent);
    }

    private void stopPlayMusicService() {
        Intent intent = new Intent(this, PlayerMusicService.class);
        stopService(intent);
    }

}
