package com.iletimerkezi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class BaseResponse {
    protected int statusCode;
    protected String message;
    protected JsonObject data;
    protected boolean isSuccessful;

    public BaseResponse(String responseBody, int httpStatusCode) {
        this.statusCode = httpStatusCode;
        JsonObject response = JsonParser.parseString(responseBody).getAsJsonObject();
        
        JsonObject responseObj = response.getAsJsonObject("response");
        if (responseObj != null) {
            JsonObject statusObj = responseObj.getAsJsonObject("status");
            if (statusObj != null && statusObj.has("message")) {
                this.message = statusObj.get("message").getAsString();
            } else {
                this.message = "Unknown error";
            }
            this.data = responseObj;
        }
        
        this.isSuccessful = httpStatusCode == 200;
        customizeData();
    }

    protected abstract void customizeData();

    public boolean ok() {
        return isSuccessful;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public JsonObject getData() {
        return data;
    }

    public JsonElement get(String name) {
        return data.get(name);
    }
}