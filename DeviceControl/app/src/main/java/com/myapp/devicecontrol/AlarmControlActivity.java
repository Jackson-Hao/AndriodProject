package com.myapp.devicecontrol;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.net.MalformedURLException;
import java.net.URL;

public class AlarmControlActivity extends AppCompatActivity {
    HttpRequestClass httpRequestClass = new HttpRequestClass();
    URL led_url;
    {
        try {
            led_url = new URL("http://20.78.209.54:8000/alarm/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.alarm), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button setAlarm = findViewById(R.id.alarm_on);
        Button cancelAlarm = findViewById(R.id.alarm_off);

        setAlarm.setOnClickListener(v -> onClickAlarm("ON"));
        cancelAlarm.setOnClickListener(v -> onClickAlarm("OFF"));

    }

    private void onClickAlarm(String cmd) {
        showProgress();
        Thread thread = new Thread(() -> {
            boolean response = AlarmControl(cmd);
            runOnUiThread(() -> {
                if (response) {
                    ad1.dismiss();
                    runOnUiThread(() -> {
                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.notice))
                                .setMessage(getString(R.string.success))
                                .setPositiveButton(getString(R.string.done), (dialog, which) -> {
                                })
                                .show();
                    });
                } else {
                    ad1.dismiss();
                    runOnUiThread(() -> {
                        new AlertDialog.Builder(this)
                                .setTitle(getString(R.string.notice))
                                .setMessage(getString(R.string.failed))
                                .setPositiveButton(getString(R.string.done), (dialog, which) -> {
                                })
                                .show();
                    });
                }
            });
        });
        thread.start();
    }

    private boolean AlarmControl(String cmd) {
        try {
            return httpRequestClass.SendHttpRequest("{\"message\":\"" + cmd + "\"}", led_url).equals("successful");
        } catch (Exception e) {
            return false;
        }
    }

    private AlertDialog ad1;
    public void showProgress(){
        ProgressBar pb = new ProgressBar(this);
        ad1 = new AlertDialog.Builder(this)
                .setView(pb)
                .setTitle(getString(R.string.processing))
                .setMessage(getString(R.string.pls_wait))
                .setCancelable(false)
                .show();
    }

}
