package com.wm.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public final class LLMUtils {

    private static final List<String> SUPPORTED_PRODUCT_TYPES = new ArrayList<>();
    private static final Map<String, Map<String, String>> PRODUCT_ATTRIBUTE_TEMPLATES = new HashMap<>();

    static {
        // Supported Product Types
        SUPPORTED_PRODUCT_TYPES.addAll(List.of(
                "athletic_shoes",
                "laptop_computers",
                "polo_tshirt"
        ));

        // Attribute templates per Product Type
        PRODUCT_ATTRIBUTE_TEMPLATES.put("athletic_shoes", Map.of(
                "brand", "",
                "color", "",
                "size", "",
                "material", "",
                "gender", "",
                "ageGroup", "",
                "pattern", "",
                "sport", "",
                "shoeClosure", "",
                "soleMaterial", ""
        ));

        PRODUCT_ATTRIBUTE_TEMPLATES.put("laptop_computers", Map.of(
                "brand", "",
                "processor", "",
                "ram", "",
                "storage", "",
                "graphicsCard", "",
                "screenSize", "",
                "batteryLife", "",
                "operatingSystem", "",
                "fingerprintReader", ""
        ));

        PRODUCT_ATTRIBUTE_TEMPLATES.put("polo_tshirt", Map.of(
                "brand", "",
                "gender", "",
                "color", "",
                "clothingSize", "",
                "clothingFit", "",
                "shirtNeckStyle", "",
                "sleeveStyle", "",
                "pattern", "",
                "material", "",
                "multipackQuantity", ""
        ));
    }

    private LLMUtils() {
        throw new UnsupportedOperationException("LLMUtils is a utility class and cannot be instantiated.");
    }

    public static List<String> getSupportedProductTypes() {
        return Collections.unmodifiableList(SUPPORTED_PRODUCT_TYPES);
    }

    public static Map<String, String> getAttributeTemplateForProductType(String productType) {
        return PRODUCT_ATTRIBUTE_TEMPLATES.getOrDefault(productType, new HashMap<>());
    }

    public static String generateProductTypePrompt(String productDescription) {
        StringBuilder sb = new StringBuilder("""
            You are a helpful assistant that classifies products accurately.

            List of Product Types:
            """);

        for (String type : SUPPORTED_PRODUCT_TYPES) {
            sb.append("- ").append(type).append("\n");
        }

        sb.append("\nProduct Description:\n\"").append(productDescription).append("\"\n\n")
                .append("Please select ONLY the SINGLE most appropriate Product Type from the list.\n")
                .append("Respond with ONLY the Product Type name, nothing else.");

        return sb.toString();
    }

    public static String generateAttributeExtractionPrompt(String productDescription, String productType) {
        Map<String, String> attributes = getAttributeTemplateForProductType(productType);
        StringBuilder jsonTemplate = new StringBuilder("{\n  \"productAttributes\": {\n");

        for (String key : attributes.keySet()) {
            jsonTemplate.append("    \"").append(key).append("\": \"\",\n");
        }

        if (!attributes.isEmpty()) {
            jsonTemplate.setLength(jsonTemplate.length() - 2); // Remove last comma
        }

        jsonTemplate.append("\n  },\n  \"confidenceScore\": 0.0\n}");

        return String.format("""
            Extract product attributes from the following description and return the result **only in the specified JSON format**.
            Include a "confidenceScore" (between 0 and 1) indicating how confident you are about the extracted values.
            If an attribute is missing, leave it as an empty string. Respond with the JSON only—no explanation or additional text.

            Product Description:
            \"%s\"

            JSON Format:
            %s
            """, productDescription, jsonTemplate.toString());
    }

    public static String extractProductTypeFromResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);

            JsonNode choicesNode = root.path("choices");
            if (choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode contentNode = choicesNode.get(0).path("message").path("content");
                if (!contentNode.isMissingNode()) {
                    return contentNode.asText().trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Object> extractProductAttributesFromResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);

            JsonNode choicesNode = root.path("choices");
            if (choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode contentNode = choicesNode.get(0).path("message").path("content");
                if (!contentNode.isMissingNode()) {
                    String content = contentNode.asText().trim();
                    // Remove Markdown block
                    if (content.startsWith("```")) {
                        int firstNewline = content.indexOf('\n');
                        int lastBackticks = content.lastIndexOf("```");
                        if (firstNewline != -1 && lastBackticks > firstNewline) {
                            content = content.substring(firstNewline + 1, lastBackticks).trim();
                        }
                    }
                    return objectMapper.readValue(content, Map.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}