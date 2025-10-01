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

    // -----------------------------
    // Public config accessors
    // -----------------------------

    public static List<String> getSupportedProductTypes() {
        return Collections.unmodifiableList(SUPPORTED_PRODUCT_TYPES);
    }

    public static Map<String, String> getAttributeTemplateForProductType(String productType) {
        return PRODUCT_ATTRIBUTE_TEMPLATES.getOrDefault(productType, new HashMap<>());
    }

    // -----------------------------
    // Prompt generators (text)
    // -----------------------------

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

    // -----------------------------
    // Prompt generators (image)
    // -----------------------------

    /**
     * Builds the same user text that was previously inlined in getProductTypeFromImage(),
     * but now dynamically lists the supported types.
     */
    public static String generateProductTypePromptForImage() {
        StringBuilder sb = new StringBuilder("You are a helpful assistant that classifies products accurately.\n\nList of Product Types:\n");
        for (String type : SUPPORTED_PRODUCT_TYPES) {
            sb.append("- ").append(type).append("\n");
        }
        sb.append("Please select ONLY the SINGLE most appropriate Product Type from the list.");
        return sb.toString();
    }

    /**
     * Keeps the ORIGINAL instruction used for image-based attribute extraction
     * (so behavior does not change). This is intentionally generic and strict
     * about JSON-only output.
     */
    public static String generateGenericImageAttributeExtractionPrompt() {
        return """
            Extract product attributes from this image and return ONLY the following JSON: {\"productAttributes\":{\"brand\":\"\",\"color\":\"\",\"size\":\"\",\"material\":\"\",\"gender\":\"\",\"ageGroup\":\"\",\"pattern\":\"\",\"sport\":\"\",\"shoeClosure\":\"\",\"soleMaterial\":\"\"},\"confidenceScore\":0-1}. Rules: (1) If an attribute is not visible/uncertain, leave it as an empty string. (2) Prefer visible marks/labels and readable text on the product/pack. (3) Do not infer sizes from proportions. (4) Output JSON only—no extra text.
            """;
    }

    /**
     * An alternative, more extensive schema (kept from your earlier helper).
     * Not used by the adapter right now, but available if needed.
     */
    public static String generateImageAttributeExtractionPrompt() {
        return """
            Extract product_type, product_attributes, and confidence_score from this image. Use this exact JSON schema: {\"product_type\":\"string\",\"product_attributes\":{\"brand\":\"string\",\"model\":\"string\",\"color\":\"string\",\"material\":\"string\",\"gender\":\"string\",\"size\":\"string\",\"shoeType\":\"string\",\"closureType\":\"string\",\"soleMaterial\":\"string\",\"sport\":\"string\",\"ageGroup\":\"string\",\"pattern\":\"string\"},\"confidence_score\":0-100}. Rules: (1) If any field is not visible/uncertain, set it to \"\". (2) Never guess sizes from proportions; only use visible labels. (3) Prefer visual marks and readable text on the product/package; avoid brand stereotypes. (4) Output JSON only—no markdown or prose.
            """;
    }

    // -----------------------------
    // Request-body builders
    // -----------------------------

    /**
     * Build a standard OpenRouter chat completion request body for a text-only prompt.
     * Matches the structure previously used in OpenRouterAdapter.buildRequestBody().
     *
     * @param model              model name
     * @param prompt             text content for the user message
     * @param temperatureOrNull  optional temperature (null = omit)
     * @param responseFormatType optional response_format.type (e.g., "json_object"; null = omit)
     * @return JSON string body
     */
    public static String buildTextRequestBody(String model,
                                              String prompt,
                                              Double temperatureOrNull,
                                              String responseFormatType) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n")
                .append("  \"model\": \"").append(escapeJson(model)).append("\",\n");

        if (temperatureOrNull != null) {
            sb.append("  \"temperature\": ").append(temperatureOrNull).append(",\n");
        }
        if (responseFormatType != null && !responseFormatType.isBlank()) {
            sb.append("  \"response_format\": {\"type\":\"").append(escapeJson(responseFormatType)).append("\"},\n");
        }

        sb.append("  \"messages\": [\n")
                .append("    {\n")
                .append("      \"role\": \"user\",\n")
                .append("      \"content\": [\n")
                .append("        {\n")
                .append("          \"type\": \"text\",\n")
                .append("          \"text\": \"").append(escapeJson(prompt)).append("\"\n")
                .append("        }\n")
                .append("      ]\n")
                .append("    }\n")
                .append("  ]\n")
                .append("}\n");

        return sb.toString();
    }

    /**
     * Build a standard OpenRouter chat completion request body for an image + text prompt.
     * Preserves original structure used in the adapter’s image methods (system + user messages).
     *
     * @param model              model name
     * @param systemPrompt       system role content
     * @param userText           user text content
     * @param imageUrl           image URL
     * @param temperatureOrNull  optional temperature (null = omit)
     * @param responseFormatType optional response_format.type (e.g., "json_object"; null = omit)
     * @return JSON string body
     */
    public static String buildImageRequestBody(String model,
                                               String systemPrompt,
                                               String userText,
                                               String imageUrl,
                                               Double temperatureOrNull,
                                               String responseFormatType) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n")
                .append("  \"model\": \"").append(escapeJson(model)).append("\",\n");

        if (temperatureOrNull != null) {
            sb.append("  \"temperature\": ").append(temperatureOrNull).append(",\n");
        }
        if (responseFormatType != null && !responseFormatType.isBlank()) {
            sb.append("  \"response_format\": {\"type\":\"").append(escapeJson(responseFormatType)).append("\"},\n");
        }

        sb.append("  \"messages\": [\n")
                .append("    {\n")
                .append("      \"role\": \"system\",\n")
                .append("      \"content\": \"").append(escapeJson(systemPrompt)).append("\"\n")
                .append("    },\n")
                .append("    {\n")
                .append("      \"role\": \"user\",\n")
                .append("      \"content\": [\n")
                .append("        {\n")
                .append("          \"type\": \"text\",\n")
                .append("          \"text\": \"").append(escapeJson(userText)).append("\"\n")
                .append("        },\n")
                .append("        {\n")
                .append("          \"type\": \"image_url\",\n")
                .append("          \"image_url\": { \"url\": \"").append(escapeJson(imageUrl)).append("\" }\n")
                .append("        }\n")
                .append("      ]\n")
                .append("    }\n")
                .append("  ]\n")
                .append("}\n");

        return sb.toString();
    }

    // -----------------------------
    // Response extractors
    // -----------------------------

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

    @SuppressWarnings("unchecked")
    public static Map<String, Object> extractProductAttributesFromResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);

            JsonNode choicesNode = root.path("choices");
            if (choicesNode.isArray() && choicesNode.size() > 0) {
                JsonNode contentNode = choicesNode.get(0).path("message").path("content");
                if (!contentNode.isMissingNode()) {
                    String content = contentNode.asText().trim();
                    // Remove Markdown fences if present
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

    // -----------------------------
    // Internal helpers
    // -----------------------------

    public static String escapeJson(String input) {
        if (input == null) return "";
        return input
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

}
