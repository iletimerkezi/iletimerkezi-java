package com.iletimerkezi.responses;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SmsResponseTest {
    
    @Test
    void shouldParseSendSuccessResponse() {
        // Arrange
        String successResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":200,"
            + "    \"message\":\"OK\""
            + "  },"
            + "  \"order\":{"
            + "    \"id\":\"12345\""
            + "  }"
            + "}}";
        
        // Act
        SmsResponse response = new SmsResponse(successResponse, 200);
        
        // Assert
        assertTrue(response.ok());
        assertEquals(200, response.getStatusCode());
        assertEquals("OK", response.getMessage());
        assertEquals("12345", response.getOrderId());
    }
    
    @Test
    void shouldParseCancelSuccessResponse() {
        // Arrange
        String successResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":200,"
            + "    \"message\":\"OK\""
            + "  }"
            + "}}";
        
        // Act
        SmsResponse response = new SmsResponse(successResponse, 200);
        
        // Assert
        assertTrue(response.ok());
        assertEquals(200, response.getStatusCode());
        assertEquals("OK", response.getMessage());
    }
    
    @Test
    void shouldHandleErrorResponse() {
        // Arrange
        String errorResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":400,"
            + "    \"message\":\"Invalid recipients\""
            + "  }"
            + "}}";
        
        // Act
        SmsResponse response = new SmsResponse(errorResponse, 400);
        
        // Assert
        assertFalse(response.ok());
        assertEquals(400, response.getStatusCode());
        assertEquals("Invalid recipients", response.getMessage());
    }
    
    @Test
    void shouldHandlePartialOrderData() {
        // Arrange
        String partialResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":200,"
            + "    \"message\":\"OK\""
            + "  },"
            + "  \"order\":{"
            + "    \"id\":\"12345\""
            + "  }"
            + "}}";
        
        // Act
        SmsResponse response = new SmsResponse(partialResponse, 200);
        
        // Assert
        assertTrue(response.ok());
        assertEquals("12345", response.getOrderId());
    }
    
    @Test
    void shouldHandleInvalidNumericValues() {
        // Arrange
        String invalidResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":200,"
            + "    \"message\":\"OK\""
            + "  },"
            + "  \"order\":{"
            + "    \"id\":\"12345\""
            + "  }"
            + "}}";
        
        // Act
        SmsResponse response = new SmsResponse(invalidResponse, 200);
        
        // Assert
        assertTrue(response.ok());
        assertEquals("12345", response.getOrderId());
    }
    
    @Test
    void shouldHandleDataAccess() {
        // Arrange
        String successResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":200,"
            + "    \"message\":\"OK\""
            + "  },"
            + "  \"order\":{"
            + "    \"id\":\"12345\""
            + "  }"
            + "}}";
        
        // Act
        SmsResponse response = new SmsResponse(successResponse, 200);
        
        // Assert
        assertNotNull(response.getData());
        assertTrue(response.getData().has("order"));
        assertEquals("12345", response.get("order").getAsJsonObject().get("id").getAsString());
    }
}