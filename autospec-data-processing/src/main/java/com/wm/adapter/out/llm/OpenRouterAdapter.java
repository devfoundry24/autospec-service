package com.wm.adapter.out.llm;

import com.wm.domain.port.out.LLMClientPort;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenRouterAdapter implements LLMClientPort {

    // Constants for OpenRouter API
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final String API_KEY = "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";
    private static final String MODEL = "google/gemma-3-12b-it:free";

    @Override
    public String getProductTypeFromLLM(String prompt) {

        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        // Construct JSON body
        String requestBody = String.format("""
                {
                  "model": "%s",
                  "messages": [
                    {
                      "role": "user",
                      "content": "%s"
                    }
                    
                  ]
                }
                """, MODEL, escapeJson(prompt));

        // Create HTTP request
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Execute HTTP request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

        // Return the raw response body
        return response.getBody();

    }

    // Helper method to escape double quotes in prompt to avoid malformed JSON
    private String escapeJson(String input) {
        return input.replace("\"", "\\\"");
    }
}
