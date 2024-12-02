package com.iletimerkezi.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WebhookReportTest {

    @Test
    void testWebhookReportParsing() {
        String jsonData = "{\"report\": {\"id\": 5, \"packet_id\": 10, \"status\": \"accepted\", " +
                         "\"to\": \"+90505702xxxx\", \"body\": \"Test mesaji\"}}";
        
        WebhookReport report = new WebhookReport(jsonData);
        
        assertEquals("5", report.getId());
        assertEquals("10", report.getPacketId());
        assertEquals("accepted", report.getStatus());
        assertEquals("+90505702xxxx", report.getTo());
        assertEquals("Test mesaji", report.getBody());
        assertTrue(report.isAccepted());
        assertFalse(report.isDelivered());
        assertFalse(report.isUndelivered());
    }

    @Test
    void testEmptyReport() {
        String jsonData = "{}";
        WebhookReport report = new WebhookReport(jsonData);
        
        assertEquals("", report.getId());
        assertEquals("", report.getPacketId());
        assertEquals("", report.getStatus());
        assertEquals("", report.getTo());
        assertEquals("", report.getBody());
        assertFalse(report.isAccepted());
        assertFalse(report.isDelivered());
        assertFalse(report.isUndelivered());
    }
} 