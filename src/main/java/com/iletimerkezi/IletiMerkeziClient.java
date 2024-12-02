package com.iletimerkezi;

import com.iletimerkezi.http.HttpClient;
import com.iletimerkezi.http.HttpURLConnectionClient;
import com.iletimerkezi.services.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;

public class IletiMerkeziClient {
    private final HttpClient httpClient;
    private final String apiKey;
    private final String apiHash;
    private final String defaultSender;

    public IletiMerkeziClient(String apiKey, String apiHash, String defaultSender) {
        this(apiKey, apiHash, defaultSender, null);
    }

    public IletiMerkeziClient(String apiKey, String apiHash, String defaultSender, HttpClient customClient) {
        this.apiKey = apiKey;
        this.apiHash = apiHash;
        this.defaultSender = defaultSender;
        this.httpClient = customClient != null ? customClient : new HttpURLConnectionClient();
    }

    public SmsService sms() {
        return new SmsService(httpClient, apiKey, apiHash, defaultSender);
    }

    public ReportService reports() {
        return new ReportService(httpClient, apiKey, apiHash);
    }

    public SummaryService summary() {
        return new SummaryService(httpClient, apiKey, apiHash);
    }

    public SenderService senders() {
        return new SenderService(httpClient, apiKey, apiHash);
    }

    public AccountService account() {
        return new AccountService(httpClient, apiKey, apiHash);
    }

    public WebhookService webhook() {
        return new WebhookService();
    }

    public BlacklistService blacklist() {
        return new BlacklistService(httpClient, apiKey, apiHash);
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public String debug() {
        Map<String, Object> debug = new HashMap<>();
        debug.put("payload", new Gson().fromJson(httpClient.getLastPayload(), JsonObject.class));
        debug.put("response", new Gson().fromJson(httpClient.getLastResponse(), JsonObject.class));
        debug.put("status", httpClient.getLastStatusCode());
        
        return new Gson().toJson(debug);
    }
}