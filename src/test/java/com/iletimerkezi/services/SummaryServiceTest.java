package com.iletimerkezi.services;

import com.iletimerkezi.http.HttpClient;
import com.iletimerkezi.responses.SummaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class SummaryServiceTest {
    
    @Mock
    private HttpClient httpClient;
    
    private SummaryService summaryService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        summaryService = new SummaryService(httpClient, "test-key", "test-hash");
    }
    
    @Test
    void testGetSummary() throws Exception {
        String successResponse = "{\n" +
               "    \"response\": {\n" +
               "        \"status\": {\n" +
               "            \"code\": 200,\n" +
               "            \"message\": \"İşlem başarılı\"\n" +
               "        },\n" +
               "        \"count\": 123,\n" +
               "        \"orders\": [\n" +
               "            {\n" +
               "                \"id\": 247079966,\n" +
               "                \"status\": 114,\n" +
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
        
        when(httpClient.post(eq("get-reports/json"), any()))
            .thenReturn(new HttpClient.Response(200, successResponse));
        
        SummaryResponse response = summaryService.list("2024-12-01", "2024-12-02", 1);
        
        assertNotNull(response, "Response should not be null");
        assertTrue(response.ok());
        assertEquals(123, response.getCount());
        assertEquals(1, response.getOrders().size());
    }
    
    @Test
    void testPagination() throws Exception {
        String successResponse = "{\n" +
               "    \"response\": {\n" +
               "        \"status\": {\n" +
               "            \"code\": 200,\n" +
               "            \"message\": \"İşlem başarılı\"\n" +
               "        },\n" +
               "        \"count\": 123,\n" +
               "        \"orders\": [\n" +
               "            {\n" +
               "                \"id\": 247079966,\n" +
               "                \"status\": 114,\n" +
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
        
        when(httpClient.post(eq("get-reports/json"), any()))
            .thenReturn(new HttpClient.Response(200, successResponse));
        
        SummaryResponse response = summaryService.list("2024-12-01", "2024-12-02", 1);
        
        assertNotNull(response, "Response should not be null");
        assertTrue(response.hasMorePages());
    }
}