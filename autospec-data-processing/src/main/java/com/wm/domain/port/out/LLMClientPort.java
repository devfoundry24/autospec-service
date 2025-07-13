package com.wm.domain.port.out;

import org.bson.json.JsonObject;

import java.util.Map;

public interface LLMClientPort {

    /**
     * Retrieves the product type from the LLM (Large Language Model) based on the provided product description.
     *
     * This method generates a prompt using the product description, builds a request body,
     * and sends a POST request to the LLM API. The response is then processed to extract
     * the product type.
     *
     * @param productDescription A description of the product for which the type needs to be determined.
     * @return The product type as a string, extracted from the LLM's response.
     */
    String getProductTypeFromLLM(String productDescription);

    /**
     * Retrieves product attributes from the LLM based on the provided product description and type.
     *
     * This method generates a prompt using the product description and type, builds a request body,
     * and sends a POST request to the LLM API. The response is then processed to extract
     * the product attributes.
     *
     * @param productDescription A description of the product for which attributes need to be extracted.
     * @param productType The type of the product, which may influence the attributes extracted.
     * @return A JSON string containing the product attributes as returned by the LLM.
     */
    Map<String,Object> getProductAttributesFromLLM(String productDescription, String productType);

}
