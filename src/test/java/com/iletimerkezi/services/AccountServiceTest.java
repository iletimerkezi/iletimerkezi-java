package com.iletimerkezi.services;

import com.iletimerkezi.http.HttpClient;
import com.iletimerkezi.responses.AccountResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class AccountServiceTest {
    
    @Mock
    private HttpClient httpClient;
    
    private AccountService accountService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountService(httpClient, "test-key", "test-hash");
    }
    
    @Test
    void shouldGetBalanceSuccessfully() throws Exception {
        // Arrange
        String successResponse = "{\"response\":{\"status\":{\"code\":200,\"message\":\"OK\"},\"balance\":{\"amount\":\"100\",\"sms\":\"1000\"}}}";
        when(httpClient.post(eq("get-balance/json"), any(Map.class)))
            .thenReturn(new HttpClient.Response(200, successResponse));
            
        // Act
        AccountResponse response = accountService.balance();
        
        // Assert
        assertTrue(response.ok());
        assertEquals("100", response.getAmount());
        assertEquals("1000", response.getCredits());
    }
}