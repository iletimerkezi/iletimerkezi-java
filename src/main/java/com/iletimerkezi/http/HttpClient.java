package com.iletimerkezi.http;

import java.io.IOException;
import java.util.Map;

public interface HttpClient {
    Response post(String url, Map<String, Object> payload) throws IOException;
    String getLastPayload();
    String getLastResponse();
    int getLastStatusCode();
    
    class Response {
        private final int statusCode;
        private final String body;
        
        public Response(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }
        
        public int getStatusCode() {
            return statusCode;
        }
        
        public String getBody() {
            return body;
        }
    }
}