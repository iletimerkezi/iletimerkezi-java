package com.iletimerkezi.models;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WebhookReport {
    private final String id;
    private final String packetId;
    private final String status;
    private final String to;
    private final String body;

    public WebhookReport(String jsonData) {
        JsonObject data = JsonParser.parseString(jsonData).getAsJsonObject();
        JsonObject report = data.getAsJsonObject("report");

        this.id = report != null ? report.get("id").getAsString() : "";
        this.packetId = report != null ? report.get("packet_id").getAsString() : "";
        this.status = report != null ? report.get("status").getAsString() : "";
        this.to = report != null ? report.get("to").getAsString() : "";
        this.body = report != null ? report.get("body").getAsString() : "";
    }

    public String getId() {
        return id;
    }

    public String getPacketId() {
        return packetId;
    }

    public String getStatus() {
        return status;
    }

    public String getTo() {
        return to;
    }

    public String getBody() {
        return body;
    }

    public boolean isDelivered() {
        return "delivered".equals(status);
    }

    public boolean isAccepted() {
        return "accepted".equals(status);
    }

    public boolean isUndelivered() {
        return "undelivered".equals(status);
    }
}