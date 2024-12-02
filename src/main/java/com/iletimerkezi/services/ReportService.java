package com.iletimerkezi.services;

import com.iletimerkezi.http.HttpClient;
import com.iletimerkezi.responses.ReportResponse;
import java.util.HashMap;
import java.util.Map;

public class ReportService {
    private static final int DEFAULT_ROW_COUNT = 1000;
    private final HttpClient httpClient;
    private final String apiKey;
    private final String apiHash;
    private Integer lastOrderId;
    private Integer lastPage;

    public ReportService(HttpClient httpClient, String apiKey, String apiHash) {
        this.httpClient = httpClient;
        this.apiKey = apiKey;
        this.apiHash = apiHash;
    }

    public ReportResponse get(int orderId, int page) throws Exception {
        this.lastOrderId = orderId;
        this.lastPage = page;

        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> authentication = new HashMap<>();
        Map<String, Object> order = new HashMap<>();

        authentication.put("key", apiKey);
        authentication.put("hash", apiHash);

        order.put("id", orderId);
        order.put("page", page);
        order.put("rowCount", DEFAULT_ROW_COUNT);

        request.put("authentication", authentication);
        request.put("order", order);
        payload.put("request", request);

        HttpClient.Response response = httpClient.post("get-report/json", payload);
        return new ReportResponse(response.getBody(), response.getStatusCode(), page);
    }

    public ReportResponse get(int orderId) throws Exception {
        return get(orderId, 1);
    }

    public ReportResponse next() throws Exception {
        if (lastOrderId == null || lastPage == null) {
            throw new RuntimeException("No previous report request found. Call getReport() first.");
        }
        return get(lastOrderId, lastPage + 1);
    }
}