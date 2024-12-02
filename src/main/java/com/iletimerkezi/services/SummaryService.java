package com.iletimerkezi.services;

import com.iletimerkezi.http.HttpClient;
import com.iletimerkezi.responses.SummaryResponse;
import java.util.HashMap;
import java.util.Map;

public class SummaryService {
    private final HttpClient httpClient;
    private final String apiKey;
    private final String apiHash;
    private String lastStartDate;
    private String lastEndDate;
    private Integer lastPage;

    public SummaryService(HttpClient httpClient, String apiKey, String apiHash) {
        this.httpClient = httpClient;
        this.apiKey = apiKey;
        this.apiHash = apiHash;
    }

    public SummaryResponse list(String startDate, String endDate, int page) throws Exception {
        this.lastStartDate = startDate;
        this.lastEndDate = endDate;
        this.lastPage = page;

        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> authentication = new HashMap<>();
        Map<String, Object> filter = new HashMap<>();

        authentication.put("key", apiKey);
        authentication.put("hash", apiHash);

        filter.put("start", startDate);
        filter.put("end", endDate);
        filter.put("page", page);

        request.put("authentication", authentication);
        request.put("filter", filter);
        payload.put("request", request);

        HttpClient.Response response = httpClient.post("get-reports/json", payload);
        return new SummaryResponse(response.getBody(), response.getStatusCode(), page);
    }

    public SummaryResponse list(String startDate, String endDate) throws Exception {
        return list(startDate, endDate, 1);
    }

    public SummaryResponse next() throws Exception {
        if (lastStartDate == null || lastEndDate == null || lastPage == null) {
            throw new RuntimeException("No previous report request found. Call list() first.");
        }
        return list(lastStartDate, lastEndDate, lastPage + 1);
    }
}