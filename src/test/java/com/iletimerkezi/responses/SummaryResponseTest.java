package com.iletimerkezi.responses;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

public class SummaryResponseTest {

    private String getSuccessResponse() {
        return "{\n" +
               "    \"response\": {\n" +
               "        \"status\": {\n" +
               "            \"code\": 200,\n" +
               "            \"message\": \"İşlem başarılı\"\n" +
               "        },\n" +
               "        \"count\": 123,\n" +
               "        \"orders\": [\n" +
               "            {\n" +
               "                \"id\": \"247079966\",\n" +
               "                \"status\": \"114\",\n" +
               "                \"total\": 1,\n" +
               "                \"delivered\": 1,\n" +
               "                \"undelivered\": 0,\n" +
               "                \"waiting\": 0,\n" +
               "                \"submitAt\": \"2024-12-01 23:39:55\",\n" +
               "                \"sendAt\": \"2024-12-01 23:39:55\",\n" +
               "                \"sender\": \"eMarka\"\n" +
               "            }\n" +
               "        ]\n" +
               "    }\n" +
               "}";
    }

    @Test
    void testSuccessfulResponse() {
        SummaryResponse response = new SummaryResponse(getSuccessResponse(), 200, 1);
        
        assertTrue(response.ok());
        assertEquals(200, response.getStatusCode());
        assertEquals("İşlem başarılı", response.getMessage());
        assertEquals(123, response.getCount());
        
        List<SummaryResponse.OrderSummary> orders = response.getOrders();
        assertEquals(1, orders.size());
        
        SummaryResponse.OrderSummary order = orders.get(0);
        assertEquals("247079966", order.getId());
        assertEquals("114", order.getStatusCode());
        assertEquals("COMPLETED", order.getStatus());
        assertEquals(1, order.getTotal());
        assertEquals(1, order.getDelivered());
        assertEquals(0, order.getUndelivered());
        assertEquals(0, order.getWaiting());
        assertEquals("2024-12-01 23:39:55", order.getSubmitAt());
        assertEquals("2024-12-01 23:39:55", order.getSendAt());
        assertEquals("eMarka", order.getSender());
    }

    @Test
    void testPagination() {
        SummaryResponse response = new SummaryResponse(getSuccessResponse(), 200, 1);
        
        assertEquals(123, response.getCount());
        assertTrue(response.hasMorePages());
    }
}