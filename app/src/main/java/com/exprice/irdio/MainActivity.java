package com.exprice.irdio;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ImageButton btn_play_pause;
    private SeekBar seekBar;
    private TextView textView;
    private MediaPlayer mediaPlayer;
    private int mediaFileLength;
    private int realtimeLength;
    final Handler handler = new Handler();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        seekBar = findViewById(R.id.seekbar);
        seekBar.setMax(99);

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mediaPlayer.isPlaying()) {
                    SeekBar seekBar = (SeekBar)v;
                    int playPosition = (mediaFileLength/100)*seekBar.getProgress();
                    mediaPlayer.seekTo(playPosition);
                }return false;
            }
        });

        textView = (TextView)findViewById(R.id.textTimer);

        btn_play_pause = (ImageButton) findViewById(R.id.btn_play_pause);
        btn_play_pause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);

                @SuppressLint("StaticFieldLeak")
                AsyncTask<String,String,String> mp3Play = new AsyncTask<String, String, String>() {

                    @Override
                    protected void onPreExecute() {
                        mDialog.setMessage("Carregando");
                        mDialog.show();
                    }

                    @Override
                    protected String doInBackground(String... params) {
                        try{
                            mediaPlayer.setDataSource(params[0]);
                            mediaPlayer.prepare();
                        }
                        catch (Exception ex) {
                        }return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        mediaFileLength = mediaPlayer.getDuration();
                        realtimeLength = mediaFileLength;
                        if(!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                            btn_play_pause.setImageResource(R.drawable.ic_pause_black_24dp);
                        }
                        else {
                            mediaPlayer.pause();
                            btn_play_pause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                        }
                        updateSeekBar();
                        mDialog.dismiss();
                    }
                };
                mp3Play.execute("http://listen.shoutcast.com/radioriodejaneirolighthits"); // link da stream
            }
        });
        mediaPlayer = new MediaPlayer();
    }

    private void updateSeekBar() {
        seekBar.setProgress((int)(((float)mediaPlayer.getCurrentPosition() / mediaFileLength)*100));
        if(mediaPlayer.isPlaying()) {
            Runnable updater = new Runnable() {
                @SuppressLint("DefaultLocale")
                @Override
                public void run() {
                    updateSeekBar();
                    realtimeLength-=1000;
                    textView.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes(realtimeLength),
                            TimeUnit.MILLISECONDS.toSeconds(realtimeLength) -
                                    TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(realtimeLength))));
                }

            };
            handler.postDelayed(updater,1000);
        }
    }
}