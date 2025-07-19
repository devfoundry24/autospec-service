package com.wm.domain.port.out;

import com.wm.domain.model.FeedItemProcessingStatus;
import com.wm.domain.model.ProductFeedItem;

import java.util.List;

public interface ProductFeedItemRepositoryPort {

    // Save a single ProductFeedItem
    void saveFeedItem(ProductFeedItem item);

    // Get details of a ProductFeedItem by its ID
    ProductFeedItem findByFeedItemId(String productFeedItemId);

    // Get all ProductFeedItems by their processing status
    List<ProductFeedItem> findByFeedItemStatus(FeedItemProcessingStatus status);

}
