package com.iletimerkezi.services;

import com.iletimerkezi.http.HttpClient;
import com.iletimerkezi.responses.SmsResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class SmsService {
    private final HttpClient httpClient;
    private final String apiKey;
    private final String apiHash;
    private final String defaultSender;
    private String sendDateTime = "";
    private String iys = "1";
    private String iysList = "BIREYSEL";

    public SmsService(HttpClient httpClient, String apiKey, String apiHash, String defaultSender) {
        this.httpClient = httpClient;
        this.apiKey = apiKey;
        this.apiHash = apiHash;
        this.defaultSender = defaultSender;
    }

    public SmsService schedule(String sendDateTime) {
        this.sendDateTime = sendDateTime;
        return this;
    }

    public SmsService enableIysConsent() {
        this.iys = "1";
        return this;
    }

    public SmsService disableIysConsent() {
        this.iys = "0";
        return this;
    }

    public SmsService iysList(String iysList) {
        this.iysList = iysList;
        return this;
    }

    public SmsResponse send(String recipient, String message) throws Exception {
        return send(List.of(recipient), message, null);
    }

    public SmsResponse send(List<String> recipients, String message) throws Exception {
        return send(recipients, message, null);
    }

    public SmsResponse send(List<String> recipients, String message, String sender) throws Exception {
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> authentication = new HashMap<>();
        Map<String, Object> order = new HashMap<>();

        authentication.put("key", apiKey);
        authentication.put("hash", apiHash);

        order.put("sender", sender != null ? sender : defaultSender);
        order.put("sendDateTime", sendDateTime);
        order.put("iys", iys);
        order.put("iysList", iysList);
        order.put("message", buildMessage(recipients, message));

        request.put("authentication", authentication);
        request.put("order", order);
        payload.put("request", request);

        HttpClient.Response response = httpClient.post("send-sms/json", payload);
        return new SmsResponse(response.getBody(), response.getStatusCode());
    }

    public SmsResponse cancel(String orderId) throws Exception {
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> authentication = new HashMap<>();
        Map<String, Object> order = new HashMap<>();

        authentication.put("key", apiKey);
        authentication.put("hash", apiHash);
        order.put("id", orderId);
        request.put("authentication", authentication);
        request.put("order", order);
        payload.put("request", request);

        HttpClient.Response response = httpClient.post("cancel-order/json", payload);
        return new SmsResponse(response.getBody(), response.getStatusCode());
    }

    private Map<String, Object> buildMessage(List<String> recipients, String message) {
        Map<String, Object> messageMap = new HashMap<>();
        Map<String, Object> receipents = new HashMap<>();
        
        messageMap.put("text", message);
        receipents.put("number", recipients);
        messageMap.put("receipents", receipents);
        
        return messageMap;
    }
}