package com.wm.adapter.out.llm;

import com.wm.common.utils.LLMUtils;
import com.wm.domain.port.out.LLMClientPort;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@Component
public class OpenRouterAdapter implements LLMClientPort {

    @Value("${openrouter.api.url}")
    private String apiUrl;

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.api.model.name}")
    private String modelName;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getProductTypeFromLLM(String productDescription) {
        String prompt = LLMUtils.generateProductTypePrompt(productDescription);
        String requestBody = buildRequestBody(prompt);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, buildHeaders());
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        return LLMUtils.extractProductTypeFromResponse(response.getBody());
    }

    @Override
    public Map<String,Object> getProductAttributesFromLLM(String productDescription, String productType) {
        String prompt =  LLMUtils.generateAttributeExtractionPrompt(productDescription,productType);
        String requestBody = buildRequestBody(prompt);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, buildHeaders());
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        return LLMUtils.extractProductAttributesFromResponse(response.getBody());  // Return full JSON content from LLM
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        return headers;
    }

    private String buildRequestBody(String prompt) {
        return String.format("""
            {
              "model": "%s",
              "messages": [
                {
                  "role": "user",
                  "content": [
                    {
                      "type": "text",
                      "text": "%s"
                    }
                  ]
                }
              ]
            }
            """, modelName, escapeJson(prompt));
    }

    private String escapeJson(String input) {
        return input.replace("\"", "\\\"").replace("\n", "\\n");
    }
}