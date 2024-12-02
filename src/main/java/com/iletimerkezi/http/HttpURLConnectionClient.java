package com.iletimerkezi.http;

import com.google.gson.Gson;
import com.iletimerkezi.VersionInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpURLConnectionClient implements HttpClient {
    private static final String BASE_URL = "https://api.iletimerkezi.com/v1/";
    private final Gson gson;
    
    private String lastPayload;
    private String lastResponse;
    private int lastStatusCode;

    public HttpURLConnectionClient() {
        this.gson = new Gson();
    }

    @Override
    public Response post(String url, Map<String, Object> payload) throws IOException {
        this.lastPayload = gson.toJson(payload);
        URL apiUrl = new URL(BASE_URL + url);
        HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
        
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.setRequestProperty("User-Agent", VersionInfo.string());
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = lastPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        this.lastStatusCode = conn.getResponseCode();
        
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(
                conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream(), 
                StandardCharsets.UTF_8
            )
        )) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        
        this.lastResponse = response.toString();
        return new Response(lastStatusCode, lastResponse);
    }

    @Override
    public String getLastPayload() {
        return lastPayload;
    }

    @Override
    public String getLastResponse() {
        return lastResponse;
    }

    @Override
    public int getLastStatusCode() {
        return lastStatusCode;
    }
}
