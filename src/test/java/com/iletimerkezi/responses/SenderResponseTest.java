package com.iletimerkezi.responses;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SenderResponseTest {
    
    @Test
    void shouldParseSuccessResponse() {
        // Arrange
        String successResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":200,"
            + "    \"message\":\"OK\""
            + "  },"
            + "  \"senders\":{"
            + "    \"sender\":[\"COMPANY\",\"BRAND\",\"TEST\"]"
            + "  }"
            + "}}";
        
        // Act
        SenderResponse response = new SenderResponse(successResponse, 200);
        
        // Assert
        assertTrue(response.ok());
        assertEquals(200, response.getStatusCode());
        assertEquals("OK", response.getMessage());
        assertEquals(3, response.getSenders().size());
        assertEquals(Arrays.asList("COMPANY", "BRAND", "TEST"), response.getSenders());
    }
    
    @Test
    void shouldHandleEmptySenders() {
        // Arrange
        String emptyResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":200,"
            + "    \"message\":\"OK\""
            + "  },"
            + "  \"senders\":{"
            + "    \"sender\":[]"
            + "  }"
            + "}}";
        
        // Act
        SenderResponse response = new SenderResponse(emptyResponse, 200);
        
        // Assert
        assertTrue(response.ok());
        assertTrue(response.getSenders().isEmpty());
    }
    
    @Test
    void shouldHandleMissingSenders() {
        // Arrange
        String missingResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":200,"
            + "    \"message\":\"OK\""
            + "  }"
            + "}}";
        
        // Act
        SenderResponse response = new SenderResponse(missingResponse, 200);
        
        // Assert
        assertTrue(response.ok());
        assertTrue(response.getSenders().isEmpty());
    }
    
    @Test
    void shouldHandleErrorResponse() {
        // Arrange
        String errorResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":401,"
            + "    \"message\":\"Authentication failed\""
            + "  }"
            + "}}";
        
        // Act
        SenderResponse response = new SenderResponse(errorResponse, 401);
        
        // Assert
        assertFalse(response.ok());
        assertEquals(401, response.getStatusCode());
        assertEquals("Authentication failed", response.getMessage());
        assertTrue(response.getSenders().isEmpty());
    }
    
    @Test
    void shouldSupportForEachLoop() {
        // Arrange
        String successResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":200,"
            + "    \"message\":\"OK\""
            + "  },"
            + "  \"senders\":{"
            + "    \"sender\":[\"COMPANY\",\"BRAND\"]"
            + "  }"
            + "}}";
        
        SenderResponse response = new SenderResponse(successResponse, 200);
        List<String> expectedSenders = Arrays.asList("COMPANY", "BRAND");
        
        // Act
        int count = 0;
        for (String sender : response.getSenders()) {
            assertTrue(expectedSenders.contains(sender));
            count++;
        }
        
        // Assert
        assertEquals(2, count);
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
            + "  \"senders\":{"
            + "    \"sender\":[\"COMPANY\",\"BRAND\"]"
            + "  }"
            + "}}";
        
        // Act
        SenderResponse response = new SenderResponse(successResponse, 200);
        
        // Assert
        assertNotNull(response.getData());
        assertTrue(response.getData().has("senders"));
        assertTrue(response.get("senders").getAsJsonObject().has("sender"));
    }
}