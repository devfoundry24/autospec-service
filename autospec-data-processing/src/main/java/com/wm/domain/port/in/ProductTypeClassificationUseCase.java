package com.wm.domain.port.in;

public interface ProductTypeClassificationUseCase {

    /**
     * Classifies the product type based on the provided feed item ID.
     *
     * @param feedItemId the ID of the product feed item to classify
     * @throws IllegalArgumentException if the feedItemId is null or empty
     */
    void classifyProductType(String feedItemId);

}
