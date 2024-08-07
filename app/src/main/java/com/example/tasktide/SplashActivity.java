package com.example.tasktide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktide.R;
import com.example.tasktide.TelaBoasVindas;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentMainAcitivty = new Intent(SplashActivity.this, TelaBoasVindas.class);
                startActivity(intentMainAcitivty);
                finish();
            }
        },3000);

    }
}