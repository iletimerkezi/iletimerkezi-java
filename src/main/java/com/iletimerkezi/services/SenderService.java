package com.iletimerkezi.services;

import com.iletimerkezi.http.HttpClient;
import com.iletimerkezi.responses.SenderResponse;
import java.util.HashMap;
import java.util.Map;

public class SenderService {
    private final HttpClient httpClient;
    private final String apiKey;
    private final String apiHash;

    public SenderService(HttpClient httpClient, String apiKey, String apiHash) {
        this.httpClient = httpClient;
        this.apiKey = apiKey;
        this.apiHash = apiHash;
    }

    /**
     * Get list of approved sender IDs
     */
    public SenderResponse list() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> authentication = new HashMap<>();

        authentication.put("key", apiKey);
        authentication.put("hash", apiHash);

        request.put("authentication", authentication);
        payload.put("request", request);

        HttpClient.Response response = httpClient.post("get-sender/json", payload);
        return new SenderResponse(response.getBody(), response.getStatusCode());
    }
}