package com.wm.adapter.out.llm;

import com.wm.common.utils.LLMUtils;
import com.wm.domain.port.out.LLMClientPort;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

@Component
public class OpenRouterAdapter implements LLMClientPort {

    @Value("${openrouter.api.url}")
    private String apiUrl;

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.api.model.name}")
    private String modelName;

    @Value("${openrouter.api.image.model.name}")
    private String imageModelName;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getProductTypeFromLLM(String productDescription) {
        String prompt = LLMUtils.generateProductTypePrompt(productDescription);
        // same body structure as before (user-only text message)
        String requestBody = LLMUtils.buildTextRequestBody(modelName, prompt, null, null);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, buildHeaders());
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        return LLMUtils.extractProductTypeFromResponse(response.getBody());
    }

    @Override
    public Map<String, Object> getProductAttributesFromLLM(String productDescription, String productType) {
        String prompt = LLMUtils.generateAttributeExtractionPrompt(productDescription, productType);
        // same body structure as before (user-only text message)
        String requestBody = LLMUtils.buildTextRequestBody(modelName, prompt, null, null);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, buildHeaders());
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        return LLMUtils.extractProductAttributesFromResponse(response.getBody());
    }

    @Override
    public String getProductTypeFromImage(List<String> imageData) {
        // Preserve original behavior: temperature=0.1, system role + user text listing supported types
        String systemPrompt = "You classify retail product images.";
        String userText = LLMUtils.generateProductTypePromptForImage(); // dynamically lists supported types
        String requestBody = LLMUtils.buildImageRequestBody(
                imageModelName,
                systemPrompt,
                userText,
                imageData,
                0.1,
                null // no response_format
        );

        HttpEntity<String> entity = new HttpEntity<>(requestBody, buildHeaders());
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        String responseBody = response.getBody();
        if (responseBody == null) {
            throw new IllegalStateException("API response body is null");
        }
        return LLMUtils.extractProductTypeFromResponse(responseBody);
    }

    @Override
    public Map<String, Object> getProductAttributesFromImage(List<String> imageData, String productType) {
        // Preserve original behavior: temperature=0.2, response_format=json_object, system role + strict JSON-only text
        String systemPrompt = "You extract retail product attributes from images. Reply with ONLY valid JSON that matches the schema.";
        String userText = LLMUtils.generateGenericImageAttributeExtractionPrompt(productType);
        String requestBody = LLMUtils.buildImageRequestBody(
                imageModelName,
                systemPrompt,
                userText,
                imageData,
                0.2,
                "json_object"
        );

        HttpEntity<String> entity = new HttpEntity<>(requestBody, buildHeaders());
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        return LLMUtils.extractProductAttributesFromResponse(response.getBody());
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        return headers;
    }
}