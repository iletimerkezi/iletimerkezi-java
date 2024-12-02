package com.iletimerkezi.responses;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AccountResponseTest {
    
    @Test
    void shouldParseSuccessResponse() {
        // Arrange
        String successResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":200,"
            + "    \"message\":\"İşlem başarılı\""
            + "  },"
            + "  \"balance\":{"
            + "    \"amount\": 100.50,"
            + "    \"sms\": 1000"
            + "  }"
            + "}}";
        
        // Act
        AccountResponse response = new AccountResponse(successResponse, 200);
        
        // Assert
        assertTrue(response.ok());
        assertEquals(200, response.getStatusCode());
        assertEquals("İşlem başarılı", response.getMessage());
        assertEquals("100.50", response.getAmount());
        assertEquals("1000", response.getCredits());
    }
    
    @Test
    void shouldHandleZeroBalance() {
        // Arrange
        String zeroResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":200,"
            + "    \"message\":\"İşlem başarılı\""
            + "  },"
            + "  \"balance\":{"
            + "    \"amount\": 0.00,"
            + "    \"sms\": 0"
            + "  }"
            + "}}";
        
        // Act
        AccountResponse response = new AccountResponse(zeroResponse, 200);
        
        // Assert
        assertTrue(response.ok());
        assertEquals("0.00", response.getAmount());
        assertEquals("0", response.getCredits());
    }
    
    @Test
    void shouldHandleMissingBalance() {
        // Arrange
        String missingResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":200,"
            + "    \"message\":\"İşlem başarılı\""
            + "  }"
            + "}}";
        
        // Act
        AccountResponse response = new AccountResponse(missingResponse, 200);
        
        // Assert
        assertTrue(response.ok());
        assertEquals("0", response.getAmount());
        assertEquals("0", response.getCredits());
    }
    
    @Test
    void shouldHandlePartialBalance() {
        // Arrange
        String partialResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":200,"
            + "    \"message\":\"İşlem başarılı\""
            + "  },"
            + "  \"balance\":{"
            + "    \"amount\":100.50"
            + "  }"
            + "}}";
        
        // Act
        AccountResponse response = new AccountResponse(partialResponse, 200);
        
        // Assert
        assertTrue(response.ok());
        assertEquals("100.50", response.getAmount());
        assertEquals("0", response.getCredits());
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
        AccountResponse response = new AccountResponse(errorResponse, 401);
        
        // Assert
        assertFalse(response.ok());
        assertEquals(401, response.getStatusCode());
        assertEquals("Üyelik bilgileri hatalı", response.getMessage());
        assertEquals("0", response.getAmount());
        assertEquals("0", response.getCredits());
    }
    
    @Test
    void shouldHandleDataAccess() {
        // Arrange
        String successResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":200,"
            + "    \"message\":\"İşlem başarılı\""
            + "  },"
            + "  \"balance\":{"
            + "    \"amount\":100.50,"
            + "    \"sms\":1000"
            + "  }"
            + "}}";
        
        // Act
        AccountResponse response = new AccountResponse(successResponse, 200);
        
        // Assert
        assertNotNull(response.getData());
        assertTrue(response.getData().has("balance"));
        assertEquals("100.50", response.get("balance").getAsJsonObject().get("amount").getAsString());
        assertEquals("1000", response.get("balance").getAsJsonObject().get("sms").getAsString());
    }
}