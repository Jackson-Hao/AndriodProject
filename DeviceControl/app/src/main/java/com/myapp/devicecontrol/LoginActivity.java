package com.myapp.devicecontrol;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import android.os.StrictMode;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class LoginActivity extends AppCompatActivity {
    HttpRequestClass HT = new HttpRequestClass();

    URL login_url;
    {
        try {
            login_url = new URL("http://20.78.209.54:8000/login_verify/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    URL check_url;
    {
        try {
            check_url = new URL("http://20.78.209.54:8000/checkNetwork/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        new Thread(() -> {
            boolean result = HT.CheckNetwork(check_url);
            runOnUiThread(() -> {
                if (!result) {
                    showNetworkErrorDialog();
                }
            });
        }).start();

        widgetFunction();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // 这里的代码会在应用的主界面中设置系统栏的内边距，以确保内容不会被系统栏遮挡。
    }
    private void widgetFunction(){
        Button button = findViewById(R.id.button_login);
        TextView tvt_usr = findViewById(R.id.editTextText);
        TextView tvt_pass = findViewById(R.id.editTextTextPassword);

        button.setOnClickListener(v -> {
            String usr = tvt_usr.getText().toString();
            String pass = tvt_pass.getText().toString();
            String jsonData = "{\"username\":\"" + usr + "\",\"password\":\"" + pass + "\"}";
            String response = null;
            try {
                response = HT.SendHttpRequest(jsonData, login_url);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            if (response.equals("successful")) {
                showLoginDialog();
            } else {
                showLoginFailDialog();
            }
        });
    }


    private AlertDialog alertDialog;
    /*-----------------------------------------------------*/
    private void showLoginDialog() {
        AlertDialog.Builder builder_login = new AlertDialog.Builder(this);
        builder_login.setTitle(getString(R.string.tv_1));
        builder_login.setMessage(getString(R.string.welcome_message));
        builder_login.setPositiveButton(R.string.done, (dialog, which) -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            alertDialog.dismiss();
            startActivity(intent);
            finish();

        });
        alertDialog = builder_login.create();
        alertDialog.show();
    }

    private void showLoginFailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.login_failed));
        builder.setMessage(getString(R.string.login_failed_msg));
        builder.setPositiveButton(getString(R.string.done), (dialog, which) -> alertDialog.dismiss());
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void showNetworkErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.notice));
        builder.setMessage(getString(R.string.network_err_msg));
        builder.setPositiveButton(getString(R.string.done), (dialog, which) -> {
            alertDialog.dismiss();
            finish();
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
    /*-----------------------------------------------------*/

}