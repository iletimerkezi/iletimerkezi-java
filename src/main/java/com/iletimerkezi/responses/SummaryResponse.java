package com.iletimerkezi.responses;

import com.iletimerkezi.BaseResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SummaryResponse extends BaseResponse {
    private static final Map<String, String> ORDER_STATUS_MESSAGES = Map.of(
        "113", "SENDING",
        "114", "COMPLETED",
        "115", "CANCELED"
    );

    private static final int PAGE_SIZE = 30;  // Her sayfada 30 kayıt

    private List<OrderSummary> orders;
    private int count;
    private int currentPage;

    public SummaryResponse(String responseBody, int httpStatusCode, int currentPage) {
        super(responseBody, httpStatusCode);
        this.currentPage = currentPage;
    }

    @Override
    protected void customizeData() {
        orders = new ArrayList<>();
        count = data.get("count").getAsInt();

        JsonArray ordersArray = data.getAsJsonArray("orders");
        for (JsonElement element : ordersArray) {
            JsonObject orderObj = element.getAsJsonObject();
            OrderSummary summary = new OrderSummary(
                orderObj.get("id").getAsString(),
                orderObj.get("status").getAsString(),
                orderObj.get("total").getAsInt(),
                orderObj.get("delivered").getAsInt(),
                orderObj.get("undelivered").getAsInt(),
                orderObj.get("waiting").getAsInt(),
                orderObj.get("submitAt").getAsString(),
                orderObj.get("sendAt").getAsString(),
                orderObj.get("sender").getAsString()
            );
            orders.add(summary);
        }
    }

    public List<OrderSummary> getOrders() {
        return orders;
    }

    public int getCount() {
        return count;
    }

    public boolean hasMorePages() {
        int totalPages = (int) Math.ceil((double) count / PAGE_SIZE);
        
        return currentPage < totalPages;
    }

    public static class OrderSummary {
        private final String id;
        private final String statusCode;
        private final String status;
        private final int total;
        private final int delivered;
        private final int undelivered;
        private final int waiting;
        private final String submitAt;
        private final String sendAt;
        private final String sender;

        public OrderSummary(String id, String statusCode, int total, int delivered, 
                          int undelivered, int waiting, String submitAt, 
                          String sendAt, String sender) {
            this.id = id;
            this.statusCode = statusCode;
            this.status = ORDER_STATUS_MESSAGES.getOrDefault(statusCode, statusCode);
            this.total = total;
            this.delivered = delivered;
            this.undelivered = undelivered;
            this.waiting = waiting;
            this.submitAt = submitAt;
            this.sendAt = sendAt;
            this.sender = sender;
        }

        public String getId() {
            return id;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public String getStatus() {
            return status;
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

        public String getSubmitAt() {
            return submitAt;
        }

        public String getSendAt() {
            return sendAt;
        }

        public String getSender() {
            return sender;
        }
    }
}