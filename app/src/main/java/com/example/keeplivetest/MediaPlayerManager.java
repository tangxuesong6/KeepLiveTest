package com.example.keeplivetest;

import android.content.Context;
import android.media.MediaPlayer;

public class MediaPlayerManager {
    private static MediaPlayerManager ourInstance = null;
    private MediaPlayer mMediaPlayer;
    private Context mContext;
    public static MediaPlayerManager getInstance(Context context) {
        // 加入双重校验锁
        // 校验锁1：第1个if
        if (ourInstance == null) {
            synchronized (MediaPlayerManager.class) {
                // 校验锁2：第2个 if
                if (ourInstance == null) {
                    ourInstance = new MediaPlayerManager(context);
                }
            }
        }
        return ourInstance;
    }

    private MediaPlayerManager(Context context) {
        this.mContext = context;
    }

    public void init() {
        mMediaPlayer = MediaPlayer.create(mContext, R.raw.twefight);
        mMediaPlayer.setLooping(true);
    }

    public void startMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public void stopMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
