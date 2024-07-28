package com.myapp.devicecontrol;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestClass {
    public String SendHttpRequest(String Json, URL url) throws IOException, JSONException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(30000);   // 设置连接超时 时间为30秒
        conn.setReadTimeout(30000);      // 设置读取超时时间为30秒
        conn.setDoOutput(true);
        conn.setDoInput(true);
        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = Json.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        JSONObject jsonResponse = new JSONObject(response.toString());
        String message = jsonResponse.getString("message");
        conn.disconnect();
        return message;
    }

    public boolean CheckNetwork(URL url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(30000);  // 设置连接超时 时间为20秒
            conn.setReadTimeout(30000);
            conn.connect();
            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode == 200;
        } catch (IOException e) {
            return false;
        }

    }
}
