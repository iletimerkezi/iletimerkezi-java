package com.iletimerkezi.services;

import com.iletimerkezi.models.WebhookReport;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WebhookService {
    /**
     * Process incoming webhook data
     */
    public WebhookReport process(String rawBody) throws IOException {
        if (rawBody == null || rawBody.isEmpty()) {
            throw new IllegalArgumentException("No POST data received");
        }

        try {
            return new WebhookReport(rawBody);
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("Invalid JSON payload", e);
        }
    }
}