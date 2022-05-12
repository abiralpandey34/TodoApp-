package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LauncherActivity extends AppCompatActivity {

    ImageView launcherImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_activity);

        launcherImage = findViewById(R.id.launcher_image);

        //Animation for Launch Screen is set here.
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.launcher_animation);
        launcherImage.startAnimation(animation);

        //Handler helps us to show loading animation for specified duration. In this case, 2000 ms.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);

    }
}