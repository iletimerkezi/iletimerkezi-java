package com.iletimerkezi.responses;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlacklistResponseTest {
    
    @Test
    void shouldParseSuccessResponse() {
        // Arrange
        String successResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":200,"
            + "    \"message\":\"İşlem başarılı\""
            + "  },"
            + "  \"blacklist\":{"
            + "    \"count\":2,"
            + "    \"number\":[\"5301234567\",\"5301234568\"]"
            + "  }"
            + "}}";
        
        // Act
        BlacklistResponse response = new BlacklistResponse(successResponse, 200);
        
        // Assert
        assertTrue(response.ok());
        assertEquals(200, response.getStatusCode());
        assertEquals("İşlem başarılı", response.getMessage());
        assertEquals(2, response.getCount());
        assertEquals(Arrays.asList("5301234567", "5301234568"), response.getNumbers());
    }
    
    @Test
    void shouldHandleEmptyBlacklist() {
        // Arrange
        String emptyResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":200,"
            + "    \"message\":\"İşlem başarılı\""
            + "  },"
            + "  \"blacklist\":{"
            + "    \"count\":0,"
            + "    \"number\":[]"
            + "  }"
            + "}}";
        
        // Act
        BlacklistResponse response = new BlacklistResponse(emptyResponse, 200);
        
        // Assert
        assertTrue(response.ok());
        assertEquals(0, response.getCount());
        assertTrue(response.getNumbers().isEmpty());
    }
    
    @Test
    void shouldHandleMissingBlacklist() {
        // Arrange
        String missingResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":200,"
            + "    \"message\":\"İşlem başarılı\""
            + "  }"
            + "}}";
        
        // Act
        BlacklistResponse response = new BlacklistResponse(missingResponse, 200);
        
        // Assert
        assertTrue(response.ok());
        assertEquals(0, response.getCount());
        assertTrue(response.getNumbers().isEmpty());
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
        BlacklistResponse response = new BlacklistResponse(errorResponse, 401);
        
        // Assert
        assertFalse(response.ok());
        assertEquals(401, response.getStatusCode());
        assertEquals("Üyelik bilgileri hatalı", response.getMessage());
        assertEquals(0, response.getCount());
        assertTrue(response.getNumbers().isEmpty());
    }
    
    @Test
    void shouldSupportForEachLoop() {
        // Arrange
        String successResponse = "{"
            + "\"response\":{"
            + "  \"status\":{"
            + "    \"code\":200,"
            + "    \"message\":\"İşlem başarılı\""
            + "  },"
            + "  \"blacklist\":{"
            + "    \"count\":2,"
            + "    \"number\":[\"5301234567\",\"5301234568\"]"
            + "  }"
            + "}}";
        
        BlacklistResponse response = new BlacklistResponse(successResponse, 200);
        
        // Act
        List<String> numbers = Arrays.asList("5301234567", "5301234568");
        int count = 0;
        
        // Assert
        for (String number : response.getNumbers()) {
            assertTrue(numbers.contains(number));
            count++;
        }
        assertEquals(2, count);
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
            + "  \"blacklist\":{"
            + "    \"count\":2,"
            + "    \"number\":[\"5301234567\",\"5301234568\"]"
            + "  }"
            + "}}";
        
        // Act
        BlacklistResponse response = new BlacklistResponse(successResponse, 200);
        
        // Assert
        assertNotNull(response.getData());
        assertTrue(response.getData().has("blacklist"));
        assertEquals(2, response.get("blacklist").getAsJsonObject().get("count").getAsInt());
    }
}