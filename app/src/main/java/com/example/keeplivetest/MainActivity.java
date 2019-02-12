package com.example.keeplivetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mOpenMusic;
    private TextView mCloseMusic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOpenMusic = (TextView) findViewById(R.id.open_music);
        mCloseMusic = (TextView) findViewById(R.id.close_music);
    }

    public void openMusic(View view) {
        startDeamon();
//        startMusic();
    }

    private void startMusic() {
        Intent intent = new Intent(MainActivity.this,PlayerMusicService.class);
        startService(intent);
    }

    private void startDeamon() {
        Intent intent2 = new Intent(MainActivity.this, DaemonService.class);
        startService(intent2);
    }

    public void closeMusic(View view) {
    }
}
