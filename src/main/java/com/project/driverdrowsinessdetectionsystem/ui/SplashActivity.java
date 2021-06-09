package com.project.driverdrowsinessdetectionsystem.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.project.driverdrowsinessdetectionsystem.LoginActivity;
import com.project.driverdrowsinessdetectionsystem.MainActivity;
import com.project.driverdrowsinessdetectionsystem.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread timer = new Thread() {

            public void run() {
                try {
                    sleep(1000);// 4 second wait
                } catch (InterruptedException e) {
                    Log.d("ERROR", e.toString());
                    Thread.currentThread().interrupt();
                } finally {
                    applicationInitialize();
                }
            }
        };

        timer.start();
    }

    //  Checking either logged in or a new user
    private void applicationInitialize() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        finish();
    }
}
