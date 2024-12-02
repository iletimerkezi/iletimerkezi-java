package com.iletimerkezi.responses;

import com.iletimerkezi.BaseResponse;
import com.google.gson.JsonObject;

public class AccountResponse extends BaseResponse {
    private String amount;
    private String sms;

    public AccountResponse(String responseBody, int httpStatusCode) {
        super(responseBody, httpStatusCode);
    }

    @Override
    protected void customizeData() {
        if (data.has("balance")) {
            JsonObject balance = data.getAsJsonObject("balance");
            this.amount = balance.has("amount") ? balance.get("amount").getAsString() : "0";
            this.sms = balance.has("sms") ? balance.get("sms").getAsString() : "0";
        } else {
            this.amount = "0";
            this.sms = "0";
        }
    }

    public String getAmount() {
        return amount;
    }

    public String getCredits() {
        return sms;
    }
}