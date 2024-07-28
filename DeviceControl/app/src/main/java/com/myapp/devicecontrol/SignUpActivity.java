package com.myapp.devicecontrol;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import android.os.StrictMode;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class SignUpActivity extends AlarmControlActivity {
    HttpRequestClass HT = new HttpRequestClass();
    URL signup_url;
    {
        try {
            signup_url = new URL("http://20.78.209.54:8000/regster/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        EdgeToEdge.enable(this);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signup_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button signupButton = findViewById(R.id.button_signup);
        TextView tvt_username = findViewById(R.id.tvt_username);
        TextView tvt_password = findViewById(R.id.tvt_passwd);
        TextView tvt_passwordagain = findViewById(R.id.tvt_passwdagain);

        signupButton.setOnClickListener(v -> {
            String username = tvt_username.getText().toString();
            String password = tvt_password.getText().toString();
            String passwordagain = tvt_passwordagain.getText().toString();
            String jsonData = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
            if (password.equals(passwordagain)) {
                try {
                        String result = HT.SendHttpRequest(jsonData, signup_url);
                    if (result.equals("successful")) {
                        showSignupSuccessDialog();
                    } else {
                        showSignupFailDialog_exi();
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            } else {
                showSignupFailDialog_err();
            }
        });




    }
    private AlertDialog alertDialog;
    private void showSignupSuccessDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.notice))
                .setMessage(getString(R.string.signup_successful))
                .setPositiveButton(getString(R.string.done), (dialog, which) -> {
                    alertDialog.dismiss();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                })
                .create();
        alertDialog.show();
    }
    private void showSignupFailDialog_err() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.notice))
                .setMessage(getString(R.string.signup_failed_pwd))
                .setPositiveButton(getString(R.string.done), (dialog, which) -> {
                    alertDialog.dismiss();
                })
                .create();
        alertDialog.show();
    }
    private void showSignupFailDialog_exi() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.notice))
                .setMessage(getString(R.string.signup_failed_exi))
                .setPositiveButton(getString(R.string.done), (dialog, which) -> {
                    alertDialog.dismiss();
                })
                .create();
        alertDialog.show();
    }


}
