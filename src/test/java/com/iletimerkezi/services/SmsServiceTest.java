package com.iletimerkezi.services;

import com.iletimerkezi.http.HttpClient;
import com.iletimerkezi.responses.SmsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class SmsServiceTest {
    
    @Mock
    private HttpClient httpClient;
    
    private SmsService smsService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        smsService = new SmsService(httpClient, "test-key", "test-hash", "test-sender");
    }
    
    @Test
    void shouldSendSmsSuccessfully() throws Exception {
        // Arrange
        String successResponse = "{\"response\":{\"status\":{\"code\":200,\"message\":\"OK\"},\"order\":{\"id\":\"12345\"}}}";
        when(httpClient.post(eq("send-sms/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(200, successResponse));
            
        // Act
        SmsResponse response = smsService.send(List.of("5301234567"), "Test message");
        
        // Assert
        assertTrue(response.ok());
        assertEquals("12345", response.getOrderId());
    }
    
    @Test
    void shouldScheduleSmsSuccessfully() throws Exception {
        // Arrange
        String successResponse = "{\"response\":{\"status\":{\"code\":200,\"message\":\"OK\"},\"order\":{\"id\":\"12345\"}}}";
        when(httpClient.post(eq("send-sms/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(200, successResponse));
            
        // Act
        SmsResponse response = smsService
            .schedule("2024-03-20 15:30:00")
            .send(List.of("5301234567"), "Test message");
        
        // Assert
        assertTrue(response.ok());
        assertEquals("12345", response.getOrderId());
    }
    
    @Test
    void shouldCancelSmsSuccessfully() throws Exception {
        // Arrange
        String successResponse = "{\"response\":{\"status\":{\"code\":200,\"message\":\"OK\"}}}";
        when(httpClient.post(eq("cancel-order/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(200, successResponse));
            
        // Act
        SmsResponse response = smsService.cancel("12345");
        
        // Assert
        assertTrue(response.ok());
    }
}