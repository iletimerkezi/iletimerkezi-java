package com.iletimerkezi.services;

import com.iletimerkezi.http.HttpClient;
import com.iletimerkezi.responses.SenderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class SenderServiceTest {
    
    @Mock
    private HttpClient httpClient;
    
    private SenderService senderService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        senderService = new SenderService(httpClient, "test-key", "test-hash");
    }
    
    @Test
    void shouldListSendersSuccessfully() throws Exception {
        // Arrange
        String successResponse = "{"
            + "\"response\":{"
            + "  \"status\":{\"code\":200,\"message\":\"OK\"},"
            + "  \"senders\":{"
            + "    \"sender\":[\"COMPANY\",\"BRAND\",\"SENDER1\"]"
            + "  }"
            + "}}";
        
        when(httpClient.post(eq("get-sender/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(200, successResponse));
        
        // Act
        SenderResponse response = senderService.list();
        
        // Assert
        assertTrue(response.ok());
        assertEquals(Arrays.asList("COMPANY", "BRAND", "SENDER1"), response.getSenders());
    }
    
    @Test
    void shouldHandleEmptySenderList() throws Exception {
        // Arrange
        String emptyResponse = "{"
            + "\"response\":{"
            + "  \"status\":{\"code\":200,\"message\":\"OK\"},"
            + "  \"senders\":{"
            + "    \"sender\":[]"
            + "  }"
            + "}}";
        
        when(httpClient.post(eq("get-sender/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(200, emptyResponse));
        
        // Act
        SenderResponse response = senderService.list();
        
        // Assert
        assertTrue(response.ok());
        assertTrue(response.getSenders().isEmpty());
    }
    
    @Test
    void shouldHandleErrorResponse() throws Exception {
        // Arrange
        String errorResponse = "{"
            + "\"response\":{"
            + "  \"status\":{\"code\":401,\"message\":\"Authentication failed\"}"
            + "}}";
        
        when(httpClient.post(eq("get-sender/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(401, errorResponse));
        
        // Act
        SenderResponse response = senderService.list();
        
        // Assert
        assertFalse(response.ok());
        assertEquals(401, response.getStatusCode());
        assertEquals("Authentication failed", response.getMessage());
    }
    
    @Test
    void shouldIterateOverSenders() throws Exception {
        // Arrange
        String successResponse = "{"
            + "\"response\":{"
            + "  \"status\":{\"code\":200,\"message\":\"OK\"},"
            + "  \"senders\":{"
            + "    \"sender\":[\"COMPANY\",\"BRAND\",\"SENDER1\"]"
            + "  }"
            + "}}";
        
        when(httpClient.post(eq("get-sender/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(200, successResponse));
        
        // Act
        SenderResponse response = senderService.list();
        
        // Assert - Using foreach
        int count = 0;
        for (String sender : response.getSenders()) {
            assertNotNull(sender);
            assertTrue(Arrays.asList("COMPANY", "BRAND", "SENDER1").contains(sender));
            count++;
        }
        assertEquals(3, count);
    }
    
    @Test
    void shouldHandleMissingSendersInResponse() throws Exception {
        // Arrange
        String invalidResponse = "{"
            + "\"response\":{"
            + "  \"status\":{\"code\":200,\"message\":\"OK\"}"
            + "}}";
        
        when(httpClient.post(eq("get-sender/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(200, invalidResponse));
        
        // Act
        SenderResponse response = senderService.list();
        
        // Assert
        assertTrue(response.ok());
        assertTrue(response.getSenders().isEmpty());
    }
}