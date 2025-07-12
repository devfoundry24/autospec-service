package com.wm.domain.port.out;

import com.wm.domain.model.ProductFeedItem;

public interface ProductFeedItemRepositoryPort {

    // Save a single ProductFeedItem
    void saveFeedItem(ProductFeedItem item);

    // Get details of a ProductFeedItem by its ID
    ProductFeedItem findByFeedItemId(String productFeedItemId);


}
