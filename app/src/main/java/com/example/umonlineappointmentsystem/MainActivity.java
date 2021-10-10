package com.example.umonlineappointmentsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;


import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Spinner;

import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    //Variables
    Animation topAnim, bottomAnim;
    ImageView iv_logo, iv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        topAnim = AnimationUtils.loadAnimation(this, R.anim.up_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.down_animation);

        iv_logo = findViewById(R.id.um_logo2);
        iv_title = findViewById(R.id.title);

        iv_logo.setAnimation(topAnim);
        iv_title.setAnimation(bottomAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(iv_logo, "um_logo");
                pairs[1] = new Pair<View, String>(iv_title, "um_title");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                startActivity(i, options.toBundle());
                finish();

            }
        },5000);

    }


}