package com.iletimerkezi.responses;

import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportResponseTest {
    
    private String getSuccessResponse() {
    return "{\n" +
           "    \"response\": {\n" +
           "        \"status\": {\n" +
           "            \"code\": 200,\n" +
           "            \"message\": \"İşlem başarılı\"\n" +
           "        },\n" +
           "        \"order\": {\n" +
           "            \"id\": \"247155523\",\n" +
           "            \"status\": \"114\",\n" +
           "            \"total\": 1,\n" +
           "            \"delivered\": 1,\n" +
           "            \"undelivered\": 0,\n" +
           "            \"waiting\": 0,\n" +
           "            \"submitAt\": \"2024-12-02 15:13:55\",\n" +
           "            \"sendAt\": \"2024-12-02 15:13:55\",\n" +
           "            \"sender\": \"eMarka\",\n" +
           "            \"price\": \"0,0372\",\n" +
           "            \"message\": [\n" +
           "                {\n" +
           "                    \"number\": \"+905375147454\",\n" +
           "                    \"status\": \"111\"\n" +
           "                }\n" +
           "            ]\n" +
           "        }\n" +
           "    }\n" +
           "}";
    }

    private String getPaginatedResponse() {
    return "{\n" +
           "    \"response\": {\n" +
           "        \"status\": {\n" +
           "            \"code\": 200,\n" +
           "            \"message\": \"İşlem başarılı\"\n" +
           "        },\n" +
           "        \"order\": {\n" +
           "            \"id\": \"247155523\",\n" +
           "            \"status\": \"114\",\n" +
           "            \"total\": 2000,\n" +
           "            \"delivered\": 1500,\n" +
           "            \"undelivered\": 300,\n" +
           "            \"waiting\": 200,\n" +
           "            \"submitAt\": \"2024-12-02 15:13:55\",\n" +
           "            \"sendAt\": \"2024-12-02 15:13:55\",\n" +
           "            \"sender\": \"eMarka\",\n" +
           "            \"price\": \"0,0372\",\n" +
           "            \"message\": [\n" +
           "                {\n" +
           "                    \"number\": \"+905375147454\",\n" +
           "                    \"status\": \"111\"\n" +
           "                },\n" +
           "                {\n" +
           "                    \"number\": \"+905375147455\",\n" +
           "                    \"status\": \"112\"\n" +
           "                }\n" +
           "            ]\n" +
           "        }\n" +
           "    }\n" +
           "}";
    }

    private String getEmptyResponse() {
    return "{\n" +
           "    \"response\": {\n" +
           "        \"status\": {\n" +
           "            \"code\": 200,\n" +
           "            \"message\": \"İşlem başarılı\"\n" +
           "        },\n" +
           "        \"order\": {\n" +
           "            \"id\": \"247155523\",\n" +
           "            \"status\": \"114\",\n" +
           "            \"total\": 0,\n" +
           "            \"delivered\": 0,\n" +
           "            \"undelivered\": 0,\n" +
           "            \"waiting\": 0,\n" +
           "            \"submitAt\": \"2024-12-02 15:13:55\",\n" +
           "            \"sendAt\": \"2024-12-02 15:13:55\",\n" +
           "            \"sender\": \"eMarka\",\n" +
           "            \"price\": \"0,0000\",\n" +
           "            \"message\": []\n" +
           "        }\n" +
           "    }\n" +
           "}";
    }
    
    @Test
    void testSuccessfulResponse() {
        ReportResponse response = new ReportResponse(getSuccessResponse(), 200, 1);
        
        assertEquals("247155523", response.getOrderId());
        assertEquals("COMPLETED", response.getOrderStatus());
        assertEquals(114, response.getOrderStatusCode());
        assertEquals(1, response.getTotal());
        assertEquals(1, response.getDelivered());
        assertEquals(0, response.getUndelivered());
        assertEquals(0, response.getWaiting());
        assertEquals("2024-12-02 15:13:55", response.getSubmitAt());
        assertEquals("2024-12-02 15:13:55", response.getSendAt());
        assertEquals("eMarka", response.getSender());
        
        List<Map<String, String>> messages = response.getMessages();
        assertEquals(1, messages.size());
        
        Map<String, String> message = messages.get(0);
        assertEquals("+905375147454", message.get("number"));
        assertEquals("111", message.get("statusCode"));
        assertEquals("DELIVERED", message.get("status"));
        
        assertFalse(response.hasMorePages());
    }
    
    @Test
    void shouldHandleEmptyReport() {
        // Act
        ReportResponse response = new ReportResponse(getEmptyResponse(), 200, 1);
        
        // Assert
        assertTrue(response.ok());
        assertEquals("247155523", response.getOrderId());
        assertEquals(0, response.getMessageCount());
        assertFalse(response.hasMorePages());
    }
    
    @Test
    void shouldHandlePagination() {

        // Act
        ReportResponse response = new ReportResponse(getPaginatedResponse(), 200, 1);
        
        // Assert
        assertTrue(response.ok());
        assertEquals(2, response.getMessageCount());
        assertTrue(response.hasMorePages());
    }
    
    @Test
    void shouldHandleErrorResponse() {
        // Arrange
        String errorResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":401,"
            + "    \"message\":\"Üyelik bilgileri hatalı\""
            + "  }"
            + "}}";
        
        // Act
        ReportResponse response = new ReportResponse(errorResponse, 401, 1);
        
        // Assert
        assertFalse(response.ok());
        assertEquals(401, response.getStatusCode());
        assertEquals("Üyelik bilgileri hatalı", response.getMessage());
        assertEquals("", response.getOrderId());
        assertEquals(0, response.getMessageCount());
    }
    
    @Test
    void shouldSupportForEachLoop() {

        ReportResponse response = new ReportResponse(getSuccessResponse(), 200, 1);
        
        // Act
        int count = 0;
        for (Map<String, String> message : response.getMessages()) {
            assertNotNull(message.get("number"));
            assertNotNull(message.get("status"));
            count++;
        }
        
        // Assert
        assertEquals(1, count);
    }
    
    @Test
    void shouldHandleDataAccess() {
        // Arrange
        String successResponse = getSuccessResponse();
        
        // Act
        ReportResponse response = new ReportResponse(successResponse, 200, 1);
        
        // Assert
        assertNotNull(response.getData());
        assertTrue(response.getData().has("order"));
        assertEquals("247155523", response.get("order").getAsJsonObject().get("id").getAsString());
    }
}