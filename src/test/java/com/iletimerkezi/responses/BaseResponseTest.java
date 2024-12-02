package com.iletimerkezi.responses;

import com.iletimerkezi.BaseResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BaseResponseTest {
    
    @Test
    void shouldParseSuccessResponse() {
        // Arrange
        String successResponse = "{\"response\":{\"status\":{\"code\":200,\"message\":\"OK\"}}}";
        
        // Act
        TestResponse response = new TestResponse(successResponse, 200);
        
        // Assert
        assertTrue(response.ok());
        assertEquals(200, response.getStatusCode());
        assertEquals("OK", response.getMessage());
    }
    
    @Test
    void shouldParseErrorResponse() {
        // Arrange
        String errorResponse = "{\"response\":{\"status\":{\"code\":400,\"message\":\"Bad Request\"}}}";
        
        // Act
        TestResponse response = new TestResponse(errorResponse, 400);
        
        // Assert
        assertFalse(response.ok());
        assertEquals(400, response.getStatusCode());
        assertEquals("Bad Request", response.getMessage());
    }
    
    private static class TestResponse extends BaseResponse {
        public TestResponse(String responseBody, int httpStatusCode) {
            super(responseBody, httpStatusCode);
        }
        
        @Override
        protected void customizeData() {
            // Test implementation
        }
    }
}