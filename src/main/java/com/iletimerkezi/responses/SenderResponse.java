package com.iletimerkezi.responses;

import com.iletimerkezi.BaseResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.List;

public class SenderResponse extends BaseResponse {
    private List<String> senders;
    private int position = 0;

    public SenderResponse(String responseBody, int httpStatusCode) {
        super(responseBody, httpStatusCode);
    }

    @Override
    protected void customizeData() {
        senders = new ArrayList<>();
        if (data.has("senders") && data.getAsJsonObject("senders").has("sender")) {
            JsonArray sendersArray = data.getAsJsonObject("senders").getAsJsonArray("sender");
            for (JsonElement sender : sendersArray) {
                senders.add(sender.getAsString());
            }
        }
    }

    public List<String> getSenders() {
        return senders;
    }
}