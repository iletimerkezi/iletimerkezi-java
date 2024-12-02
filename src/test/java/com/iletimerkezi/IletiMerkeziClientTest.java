package com.iletimerkezi;

import com.iletimerkezi.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class IletiMerkeziClientTest {
    
    @Mock
    private HttpClient httpClient;
    
    private IletiMerkeziClient client;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = new IletiMerkeziClient("test-key", "test-hash", "test-sender", httpClient);
    }
    
    @Test
    void shouldCreateServicesSuccessfully() {
        assertNotNull(client.sms());
        assertNotNull(client.reports());
        assertNotNull(client.summary());
        assertNotNull(client.senders());
        assertNotNull(client.account());
        assertNotNull(client.webhook());
        assertNotNull(client.blacklist());
    }
    
    @Test
    void shouldReturnHttpClient() {
        assertEquals(httpClient, client.getHttpClient());
    }
}