package com.iletimerkezi.services;

import com.iletimerkezi.http.HttpClient;
import com.iletimerkezi.responses.BlacklistResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class BlacklistService {
    private static final int DEFAULT_ROW_COUNT = 1000;
    private final HttpClient httpClient;
    private final String apiKey;
    private final String apiHash;
    private Integer lastPage = null;

    public BlacklistService(HttpClient httpClient, String apiKey, String apiHash) {
        this.httpClient = httpClient;
        this.apiKey = apiKey;
        this.apiHash = apiHash;
    }

    public BlacklistResponse add(String number) throws Exception {
        List<String> numbers = new ArrayList<>();
        numbers.add(number);
        return add(numbers);
    }

    private BlacklistResponse add(List<String> numbers) throws Exception {
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> authentication = new HashMap<>();
        Map<String, Object> blacklist = new HashMap<>();

        authentication.put("key", apiKey);
        authentication.put("hash", apiHash);

        if(numbers.size() > 0) {
            blacklist.put("number", numbers.get(0));
        } else {
            blacklist.put("number", "");
        }

        request.put("authentication", authentication);
        request.put("blacklist", blacklist);
        payload.put("request", request);

        HttpClient.Response response = httpClient.post("add-blacklist/json", payload);
        return new BlacklistResponse(response.getBody(), response.getStatusCode());
    }


    public BlacklistResponse remove(String number) throws Exception {
        List<String> numbers = new ArrayList<>();
        numbers.add(number);
        return remove(numbers);
    }

    public BlacklistResponse remove(List<String> numbers) throws Exception {
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> authentication = new HashMap<>();
        Map<String, Object> blacklist = new HashMap<>();

        authentication.put("key", apiKey);
        authentication.put("hash", apiHash);
        
        if(numbers.size() > 0) {
            blacklist.put("number", numbers.get(0));
        } else {
            blacklist.put("number", "");
        }

        request.put("authentication", authentication);
        request.put("blacklist", blacklist);
        payload.put("request", request);

        HttpClient.Response response = httpClient.post("delete-blacklist/json", payload);
        return new BlacklistResponse(response.getBody(), response.getStatusCode());
    }

    public BlacklistResponse list(int page) throws Exception {
        lastPage = page;
        return list(page, null, null);
    }

    public BlacklistResponse list(int page, String startDate, String endDate) throws Exception {
        lastPage = page;
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> authentication = new HashMap<>();
        Map<String, Object> blacklist = new HashMap<>();

        authentication.put("key", apiKey);
        authentication.put("hash", apiHash);

        blacklist.put("page", page);
        blacklist.put("rowCount", DEFAULT_ROW_COUNT);

        if (startDate != null && endDate != null) {
            Map<String, String> filter = new HashMap<>();
            filter.put("start", startDate);
            filter.put("end", endDate);
            blacklist.put("filter", filter);
        }

        request.put("authentication", authentication);
        request.put("blacklist", blacklist);
        payload.put("request", request);

        HttpClient.Response response = httpClient.post("get-blacklist/json", payload);
        return new BlacklistResponse(response.getBody(), response.getStatusCode(), page);
    }

    public BlacklistResponse next() throws Exception {
        if (lastPage == null) {
            throw new IllegalStateException("List method must be called before next.");
        }
        return list(lastPage + 1);
    }

    public BlacklistResponse next(String startDate, String endDate) throws Exception {
        if (lastPage == null) {
            throw new IllegalStateException("List method must be called before next.");
        }
        return list(lastPage + 1, startDate, endDate);
    }
}