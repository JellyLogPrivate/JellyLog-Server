package com.saram.jellylog.report.client;

import tools.jackson.databind.JsonNode;

public interface AiClient {
    String generateContent(String prompt);
    JsonNode parseJsonResponse(String rawResponse);
}
