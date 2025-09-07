package com.wm.domain.service;

import com.wm.domain.model.FeedItemProcessingStatus;
import com.wm.domain.model.ProductFeedItem;
import com.wm.domain.port.in.ProductFeedItemUseCase;
import com.wm.domain.port.out.ProductFeedItemRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ProductFeedItemService implements ProductFeedItemUseCase {

    private final ProductFeedItemRepositoryPort feedItemRepository;

    @Override
    public void saveFeedItem(ProductFeedItem productFeedItem) {
        log.info("Saving product feed item with id: {}", productFeedItem.getId());
        try {
            productFeedItem.setFeedItemCreationTime(java.time.Instant.now());
            productFeedItem.setFeedItemStatus(String.valueOf(FeedItemProcessingStatus.ACCEPTED));
            feedItemRepository.saveFeedItem(productFeedItem);
            log.info("Product feed item saved successfully with id: {}", productFeedItem.getId());
        } catch (Exception e) {
            log.error("Error saving product feed item: {}", e.getMessage(), e);
        }
    }

    @Override
    public ProductFeedItem getProductFeedItem(String productFeedItemId) {
        log.info("Fetching product feed details for feedId: {}", productFeedItemId);

        ProductFeedItem feedItem = feedItemRepository.findByFeedItemId(productFeedItemId);

        if (feedItem != null) {
            log.info("Product feed item found for feedId: {}", productFeedItemId);
            return feedItem;
        } else {
            log.warn("No product feed item found for feedId: {}", productFeedItemId);
            return null;
        }
    }

}