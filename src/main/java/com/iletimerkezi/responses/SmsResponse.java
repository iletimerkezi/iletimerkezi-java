package com.iletimerkezi.responses;

import com.iletimerkezi.BaseResponse;
import com.google.gson.JsonObject;

public class SmsResponse extends BaseResponse {
    private String orderId;

    public SmsResponse(String responseBody, int httpStatusCode) {
        super(responseBody, httpStatusCode);
    }

    @Override
    protected void customizeData() {
        JsonObject orderObj = data.getAsJsonObject("order");
        if (orderObj != null && orderObj.has("id")) {
            this.orderId = orderObj.get("id").getAsString();
            this.data.addProperty("id", this.orderId);
        }
    }

    public String getOrderId() {
        return orderId;
    }
}