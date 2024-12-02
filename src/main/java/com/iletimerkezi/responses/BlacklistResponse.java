package com.iletimerkezi.responses;

import com.iletimerkezi.BaseResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import java.util.List;

public class BlacklistResponse extends BaseResponse {
    private List<String> numbers;
    private int position = 0;
    private final int currentPage;

    public BlacklistResponse(String responseBody, int httpStatusCode) {
        this(responseBody, httpStatusCode, 1);
    }

    public BlacklistResponse(String responseBody, int httpStatusCode, int currentPage) {
        super(responseBody, httpStatusCode);
        this.currentPage = currentPage;
    }

    @Override
    protected void customizeData() {
        numbers = new ArrayList<>();
        if (data.has("blacklist") && data.getAsJsonObject("blacklist").has("number")) {
            JsonArray numbersArray = data.getAsJsonObject("blacklist").getAsJsonArray("number");
            for (JsonElement number : numbersArray) {
                numbers.add(number.getAsString());
            }
        }
    }

    public int getCount() {
        if (data.has("blacklist") && data.getAsJsonObject("blacklist").has("count")) {
            return data.getAsJsonObject("blacklist").get("count").getAsInt();
        }
        return 0;
    }

    public List<String> getNumbers() {
        return numbers;
    }

    public boolean hasMorePage() {
        int totalPages = (int) Math.ceil((double) this.getCount() / 1000);
        return this.currentPage < totalPages;
    }
}