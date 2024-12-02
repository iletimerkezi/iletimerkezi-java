package com.iletimerkezi.responses;

import com.iletimerkezi.BaseResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReportResponse extends BaseResponse {
    private static final Map<String, String> ORDER_STATUS_MESSAGES = Map.of(
        "113", "SENDING",
        "114", "COMPLETED",
        "115", "CANCELED"
    );

    private static final Map<String, String> MESSAGE_STATUS_MESSAGES = Map.of(
        "110", "WAITING",
        "111", "DELIVERED",
        "112", "UNDELIVERED"
    );

    private List<Map<String, String>> messages;
    private String submitAt;
    private String sendAt;
    private String sender;
    private int total;
    private int delivered;
    private int undelivered;
    private int waiting;
    private final int currentPage;

    public ReportResponse(String responseBody, int httpStatusCode, int currentPage) {
        super(responseBody, httpStatusCode);
        this.currentPage = currentPage;
    }

    @Override
    protected void customizeData() {
        messages = new ArrayList<>();
        JsonObject orderObj = data.getAsJsonObject("order");
        
        if (orderObj != null) {
            // Temel sipariş bilgileri
            this.total = orderObj.get("total").getAsInt();
            this.delivered = orderObj.get("delivered").getAsInt();
            this.undelivered = orderObj.get("undelivered").getAsInt();
            this.waiting = orderObj.get("waiting").getAsInt();
            this.submitAt = orderObj.get("submitAt").getAsString();
            this.sendAt = orderObj.get("sendAt").getAsString();
            this.sender = orderObj.get("sender").getAsString();

            // Mesaj detayları
            if (orderObj.has("message")) {
                JsonArray messageArray = orderObj.getAsJsonArray("message");
                for (JsonElement message : messageArray) {
                    JsonObject messageObj = message.getAsJsonObject();
                    messages.add(Map.of(
                        "number", messageObj.get("number").getAsString(),
                        "statusCode", messageObj.get("status").getAsString(),
                        "status", MESSAGE_STATUS_MESSAGES.getOrDefault(
                            messageObj.get("status").getAsString(),
                            messageObj.get("status").getAsString()
                        )
                    ));
                }
            }
        }
    }

    public String getOrderId() {
        JsonObject orderObj = data.getAsJsonObject("order");
        return orderObj != null ? orderObj.get("id").getAsString() : "";
    }

    public String getOrderStatus() {
        JsonObject orderObj = data.getAsJsonObject("order");
        if (orderObj != null && orderObj.has("status")) {
            String status = orderObj.get("status").getAsString();
            return ORDER_STATUS_MESSAGES.getOrDefault(status, status);
        }
        return "";
    }

    public int getOrderStatusCode() {
        JsonObject orderObj = data.getAsJsonObject("order");
        if (orderObj != null && orderObj.has("status")) {
            return orderObj.get("status").getAsInt();
        }
        return 0;
    }

    public List<Map<String, String>> getMessages() {
        return messages;
    }

    public int getMessageCount() {
        return messages.size();
    }

    public String getSubmitAt() {
        return submitAt;
    }

    public String getSendAt() {
        return sendAt;
    }

    public String getSender() {
        return sender;
    }

    public int getTotal() {
        return total;
    }

    public int getDelivered() {
        return delivered;
    }

    public int getUndelivered() {
        return undelivered;
    }

    public int getWaiting() {
        return waiting;
    }

    public boolean hasMorePages() {
        int totalPages = (int) Math.ceil((double) this.total / 1000);

        return this.currentPage < totalPages;
    }
}