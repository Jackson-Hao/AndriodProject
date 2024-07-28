package com.myapp.devicecontrol;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button bt_light = findViewById(R.id.main_light);
        Button bt_alarm = findViewById(R.id.main_alarm);
        Button bt_more = findViewById(R.id.main_more);


        bt_light.setOnClickListener(v -> clickToLight());
        bt_alarm.setOnClickListener(v -> clickToAlarm());
        bt_more.setOnClickListener(v -> clickToMore());


    }

    private void clickToLight() {
        Intent intent = new Intent(this, LightControlActivity.class);
        startActivity(intent);
    }

    private void clickToAlarm() {
        Intent intent = new Intent(this, AlarmControlActivity.class);
        startActivity(intent);
    }

    private AlertDialog alertDialog;
    private void clickToMore() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.notice));
        builder.setMessage(getString(R.string.notice_more));
        builder.setPositiveButton(getString(R.string.done), (dialog, which) -> alertDialog.dismiss());
        alertDialog = builder.create();
        alertDialog.show();
    }
}
