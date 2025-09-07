package com.wm.adapter.out.persistence;

import com.wm.domain.model.FeedItemProcessingStatus;
import com.wm.domain.model.ProductFeedItem;
import com.wm.domain.port.out.ProductFeedItemRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductFeedItemMongoAdapter implements ProductFeedItemRepositoryPort {

    private final ProductFeedItemRepository productFeedItemRepository;

    public ProductFeedItemMongoAdapter(ProductFeedItemRepository productFeedItemRepository) {
        this.productFeedItemRepository = productFeedItemRepository;
    }

    @Override
    public void saveFeedItem(ProductFeedItem item) {
        if (item == null) {
            throw new IllegalArgumentException("ProductFeedItem cannot be null");
        }
        productFeedItemRepository.save(item);
    }

    @Override
    public ProductFeedItem findByFeedItemId(String productFeedItemId) {
        if (productFeedItemId == null || productFeedItemId.isEmpty()) {
            throw new IllegalArgumentException("ProductFeedItem ID cannot be null or empty");
        }
        return productFeedItemRepository.findById(productFeedItemId)
                .orElseThrow(() -> new IllegalArgumentException("ProductFeedItem not found for ID: " + productFeedItemId));
    }

    @Override
    public List<ProductFeedItem> findByFeedItemStatus(FeedItemProcessingStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("FeedItemProcessingStatus cannot be null");
        }
        return productFeedItemRepository.findByFeedItemStatus(status);
    }
}
