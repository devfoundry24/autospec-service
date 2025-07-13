package com.wm.domain.port.in;

public interface ProductEnrichmentUseCase {

    /**
     * Classifies the product type for the given feed item.
     *
     * This method processes the feed item identified by the provided ID
     * and determines its product type using predefined classification logic.
     *
     * @param feedItemId The unique identifier of the feed item to classify.
     */
    void classifyProductType(String feedItemId);

    /**
     * Extracts product attributes for the given feed item.
     *
     * This method processes the feed item identified by the provided ID
     * and extracts its attributes such as brand, color, size, etc.
     *
     * @param feedItemId The unique identifier of the feed item to extract attributes from.
     */
    void extractProductAttributes(String feedItemId);

}
