package com.myapp.devicecontrol;

import android.content.Intent;
import android.os.Bundle;

import android.os.StrictMode;
import android.widget.Button;
import android.widget.ProgressBar;


import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.net.MalformedURLException;
import java.net.URL;


public class LightControlActivity extends AppCompatActivity {
    HttpRequestClass httpRequestClass = new HttpRequestClass();
    URL led_url;
    {
        try {
            led_url = new URL("http://20.78.209.54:8000/led_control/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // 使 EdgeToEdge 库生效
        setContentView(R.layout.activity_light);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Button bt_on = findViewById(R.id.button_on);
        Button bt_off = findViewById(R.id.button_off);
        Button bt_flash = findViewById(R.id.button_flash);

        bt_on.setOnClickListener(v -> onClick("ON"));
        bt_off.setOnClickListener(v -> onClick("OFF"));
        bt_flash.setOnClickListener(v -> clickToAbout());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.light), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        }

    private void onClick(String cmd) {
        showProgress();
        Thread thread = new Thread(() -> {
            boolean response = LedControl(cmd);
            if (response){
                ad.dismiss();
                runOnUiThread(() -> {
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.notice))
                            .setMessage(getString(R.string.success))
                            .setPositiveButton(getString(R.string.done), null)
                            .show();
                });
            } else {
                ad.dismiss();
                runOnUiThread(() -> {
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.notice))
                            .setMessage(getString(R.string.failed))
                            .setPositiveButton(getString(R.string.done), null)
                            .show();
                });
            }
        });
        thread.start();
    }

    private void clickToAbout() {
        Intent intent = new Intent(LightControlActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    private boolean LedControl(String cmd) {
        try {
            return httpRequestClass.SendHttpRequest("{\"message\":\"" + cmd + "\"}", led_url).equals("successful");
        } catch (Exception e) {
            return false;
        }
    }

    private AlertDialog ad;
    public void showProgress(){
        ProgressBar pb = new ProgressBar(this);
        ad = new AlertDialog.Builder(this)
                .setView(pb)
                .setTitle(getString(R.string.processing))
                .setMessage(getString(R.string.pls_wait))
                .setCancelable(false)
                .show();
    }
}
