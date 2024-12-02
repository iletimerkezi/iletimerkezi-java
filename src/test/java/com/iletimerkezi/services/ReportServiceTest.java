package com.iletimerkezi.services;

import com.iletimerkezi.http.HttpClient;
import com.iletimerkezi.responses.ReportResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ReportServiceTest {
    
    @Mock
    private HttpClient httpClient;
    
    private ReportService reportService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportService = new ReportService(httpClient, "test-key", "test-hash");
    }

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
    void shouldGetReportSuccessfully() throws Exception {
        // Arrange
        String successResponse = getSuccessResponse();
        
        when(httpClient.post(eq("get-report/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(200, successResponse));
        
        // Act
        ReportResponse response = reportService.get(12345);
        
        // Assert
        assertTrue(response.ok());
        assertEquals("247155523", response.getOrderId());
        assertEquals("COMPLETED", response.getOrderStatus());
        assertEquals(1, response.getMessageCount());
    }
    
    @Test
    void shouldGetReportWithPaginationSuccessfully() throws Exception {
        // Arrange
        String successResponse = getSuccessResponse();        
        
        when(httpClient.post(eq("get-report/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(200, successResponse));
        
        // Act
        ReportResponse response = reportService.get(12345, 2);
        
        // Assert
        assertTrue(response.ok());
        assertEquals(1, response.getMessageCount());
        assertFalse(response.hasMorePages());
    }
    
    @Test
    void shouldGetNextPageSuccessfully() throws Exception {
        // Arrange
        String firstResponse = getSuccessResponse();
        
        String secondResponse = "{\n" +
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
           "                    \"number\": \"+905301234568\",\n" +
           "                    \"status\": \"111\"\n" +
           "                }\n" +
           "            ]\n" +
           "        }\n" +
           "    }\n" +
           "}";
        
        when(httpClient.post(eq("get-report/json"), any(Map.class)))
            .thenReturn(
                new HttpClient.Response(200, firstResponse),
                new HttpClient.Response(200, secondResponse)
            );
        
        // Act & Assert
        ReportResponse firstPage = reportService.get(12345, 1);
        assertTrue(firstPage.ok());
        assertEquals("+905375147454", firstPage.getMessages().get(0).get("number"));
        
        ReportResponse secondPage = reportService.next();
        assertTrue(secondPage.ok());
        assertEquals("+905301234568", secondPage.getMessages().get(0).get("number"));
    }
    
    @Test
    void shouldHandleEmptyReport() throws Exception {

        when(httpClient.post(eq("get-report/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(200, getEmptyResponse()));
        
        // Act
        ReportResponse response = reportService.get(12345);
        
        // Assert
        assertTrue(response.ok());
        assertEquals(0, response.getMessageCount());
        assertFalse(response.hasMorePages());
    }
    
    @Test
    void shouldHandleErrorResponse() throws Exception {
        // Arrange
        String errorResponse = "{"
            + "\"response\":{"
            + "  \"status\":{\"code\":400,\"message\":\"Invalid order ID\"}"
            + "}}";
        
        when(httpClient.post(eq("get-report/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(400, errorResponse));
        
        // Act
        ReportResponse response = reportService.get(12345);
        
        // Assert
        assertFalse(response.ok());
        assertEquals(400, response.getStatusCode());
        assertEquals("Invalid order ID", response.getMessage());
    }
    
    @Test
    void shouldThrowExceptionWhenCallingNextWithoutPreviousCall() {
        // Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            // Act
            reportService.next();
        });
        
        assertEquals("No previous report request found. Call getReport() first.", exception.getMessage());
    }
    
    @Test
    void shouldHandleIteratorMethods() throws Exception {
        
        when(httpClient.post(eq("get-report/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(200, getSuccessResponse()));
        
        // Act
        ReportResponse response = reportService.get(12345);
        
        // Assert - Iterator functionality
        int count = 0;
        for (Map<String, String> message : response.getMessages()) {
            assertNotNull(message.get("number"));
            assertNotNull(message.get("status"));
            count++;
        }
        assertEquals(1, count);
    }
}