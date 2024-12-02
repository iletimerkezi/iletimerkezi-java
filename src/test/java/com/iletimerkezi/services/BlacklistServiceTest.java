package com.iletimerkezi.services;

import com.iletimerkezi.http.HttpClient;
import com.iletimerkezi.responses.BlacklistResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class BlacklistServiceTest {
    
    @Mock
    private HttpClient httpClient;
    
    private BlacklistService blacklistService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        blacklistService = new BlacklistService(httpClient, "test-key", "test-hash");
    }
    
    @Test
    void shouldAddNumbersToBlacklistSuccessfully() throws Exception {
        // Arrange
        String successResponse = "{\"response\":{\"status\":{\"code\":200,\"message\":\"İşlem başarılı\"}}}";
        when(httpClient.post(eq("add-blacklist/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(200, successResponse));
        
        // Act
        BlacklistResponse response = blacklistService.add("5301234567");
        
        // Assert
        assertTrue(response.ok());
    }
    
    @Test
    void shouldRemoveNumbersFromBlacklistSuccessfully() throws Exception {
        // Arrange
        String successResponse = "{\"response\":{\"status\":{\"code\":200,\"message\":\"İşlem başarılı\"}}}";
        when(httpClient.post(eq("delete-blacklist/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(200, successResponse));
        
        List<String> numbers = Arrays.asList("5301234567");
        
        // Act
        BlacklistResponse response = blacklistService.remove(numbers);
        
        // Assert
        assertTrue(response.ok());
    }
    
    @Test
    void shouldListBlacklistedNumbersSuccessfully() throws Exception {
        // Arrange
        String successResponse = "{\"response\":{\"status\":{\"code\":200,\"message\":\"İşlem başarılı\"},\"blacklist\":{\"count\":2,\"number\":[\"5301234567\",\"5301234568\"]}}}";
        when(httpClient.post(eq("get-blacklist/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(200, successResponse));
        
        // Act
        BlacklistResponse response = blacklistService.list(1);
        
        // Assert
        assertTrue(response.ok());
        assertEquals(2, response.getCount());
        assertEquals(Arrays.asList("5301234567", "5301234568"), response.getNumbers());
    }
    
    @Test
    void shouldHandleEmptyBlacklist() throws Exception {
        // Arrange
        String emptyResponse = "{\"response\":{\"status\":{\"code\":200,\"message\":\"İşlem başarılı\"},\"blacklist\":{\"count\":0,\"number\":[]}}}";
        when(httpClient.post(eq("get-blacklist/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(200, emptyResponse));
        
        // Act
        BlacklistResponse response = blacklistService.list(1);
        
        // Assert
        assertTrue(response.ok());
        assertEquals(0, response.getCount());
        assertTrue(response.getNumbers().isEmpty());
    }
    
    @Test
    void shouldHandleErrorResponse() throws Exception {
        // Arrange
        String errorResponse = "{\"response\":{\"status\":{\"code\":401,\"message\":\"Üyelik bilgileri hatalı\"}}}";
        when(httpClient.post(eq("add-blacklist/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(401, errorResponse));
        
        // Act
        BlacklistResponse response = blacklistService.add("5301234567");
        
        // Assert
        assertFalse(response.ok());
        assertEquals(401, response.getStatusCode());
        assertEquals("Üyelik bilgileri hatalı", response.getMessage());
    }
}