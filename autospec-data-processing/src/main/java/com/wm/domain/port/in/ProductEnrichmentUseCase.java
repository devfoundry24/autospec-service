package com.wm.domain.port.in;

import java.util.List;

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

    /**
     * Processes a batch of feed items to classify their product types.
     *
     * This method takes a list of feed item IDs and applies product type
     * classification logic to each item in the batch. It is designed to
     * handle multiple feed items in a single operation, improving efficiency
     * for batch processing scenarios.
     *
     * @param feedItemIds A list of unique identifiers for the feed items
     *                    to classify.
     */
    boolean classifyProductTypeBatch(List<String> feedItemIds);

    /**
         * Extracts product attributes for a batch of feed items.
         *
         * This method processes a list of feed item IDs and extracts their attributes,
         * such as brand, color, size, etc. It is designed for batch processing to
         * improve efficiency when handling multiple feed items.
         *
         * @param feedItemIds A list of unique identifiers for the feed items
         *                    to extract attributes from.
     */
    boolean extractProductAttributesBatch(List<String> feedItemIds);

    /**
     * Classifies the product type from the image data of the given feed item.
     *
     * This method processes the feed item identified by the provided ID
     * and classifies its product type using image recognition techniques.
     *
     * @param feedItemId The unique identifier of the feed item to classify from its image.
     */
    void classifyProductTypeFromImage(String feedItemId);

    /**
     * Extracts product attributes from the image data of the given feed item.
     *
     * This method processes the feed item identified by the provided ID
     * and extracts its attributes using image recognition techniques.
     *
     * @param feedItemId The unique identifier of the feed item to extract attributes from its image.
     */
    void extractProductAttributesFromImage(String feedItemId);

}
