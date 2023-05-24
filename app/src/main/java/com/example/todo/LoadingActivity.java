package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.todo.ToDoActivity.ToDoActivity;
import com.github.ybq.android.spinkit.SpinKitView;

import java.time.LocalTime;

public class LoadingActivity extends AppCompatActivity {
    @SuppressLint("NewApi")
    LocalTime time = LocalTime.now();
    @SuppressLint("NewApi")
    int hour = time.getHour();
    private final int delaySeconds = 5000;

    private SpinKitView spinKitView1;
    private Handler handler;
    private Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        notification = new Notification();
        notification.scheduleNotification(getApplicationContext());
        spinKitView1 = findViewById(R.id.spinKitView1);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoadingActivity.this, ToDoActivity.class));
                finish();
            }
        }, delaySeconds);
        if (hour > 0 && hour < 12) {
            Toast.makeText(this, "Hello, Good Morning", Toast.LENGTH_SHORT).show();
        } else if (hour >= 12 && hour < 18) {
            Toast.makeText(this, "Hello, Good Afternoon", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Hello, Good Evening", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacksAndMessages(null);
    }
}