package com.wm.domain.port.in;

import com.wm.domain.model.ProductFeedItem;

public interface ProductFeedItemUseCase {

    // Ingest a single ProductFeedItem
    void saveFeedItem(ProductFeedItem item);

    // Get details of a ProductFeedItem by its ID
    ProductFeedItem getProductFeedItem(String productFeedId);

}
