package com.iletimerkezi.services;

import com.iletimerkezi.http.HttpClient;
import com.iletimerkezi.responses.AccountResponse;
import java.util.HashMap;
import java.util.Map;

public class AccountService {
    private final HttpClient httpClient;
    private final String apiKey;
    private final String apiHash;

    public AccountService(HttpClient httpClient, String apiKey, String apiHash) {
        this.httpClient = httpClient;
        this.apiKey = apiKey;
        this.apiHash = apiHash;
    }

    public AccountResponse balance() throws Exception {
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> authentication = new HashMap<>();

        authentication.put("key", apiKey);
        authentication.put("hash", apiHash);

        request.put("authentication", authentication);
        payload.put("request", request);

        HttpClient.Response response = httpClient.post("get-balance/json", payload);
        return new AccountResponse(response.getBody(), response.getStatusCode());
    }
}