package com.example.videoview.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.videoview.R;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;
    SharedPreferences sharedPreferences;
    String success;
    String isOtpDone = "undone";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);
        videoView = findViewById(R.id.videoViewID);
        sharedPreferences = getSharedPreferences("log", MODE_PRIVATE);
        success = sharedPreferences.getString("key", "");

/*
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) videoView.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.leftMargin = 0;
        videoView.setLayoutParams(params);*/

        isOtpDone = getSharedPreferences("otp", MODE_PRIVATE).getString("otp_service", "undone");
        if (isOtpDone.equals("done")) {
            startActivity(new Intent(getApplicationContext(), UserSelectionActivity.class));
            finishAffinity();
        } else {

            videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.video);
            videoView.start();

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (isFinishing()) {
                        return;
                    } else {

                     /*   if (success.equals("success")) {
                            startActivity(new Intent(getApplicationContext(), OtpActivity.class));
                            Animatoo.animateDiagonal(MainActivity.this);

                        }*/

                        startActivity(new Intent(getApplicationContext(), SliderActivity.class));
                        Animatoo.animateDiagonal(MainActivity.this);

                    }
                }
            });


        }
    }
}
