package com.wm.adapter.out.persistence;

import com.wm.domain.model.FeedItemProcessingStatus;
import com.wm.domain.model.ProductFeedItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductFeedItemRepository extends MongoRepository<ProductFeedItem, String> {

    // Find a specific ProductFeedItem by id
    Optional<ProductFeedItem> findById(String id);

    // Find all ProductFeedItems by their processing status
    List<ProductFeedItem> findByFeedItemStatus(FeedItemProcessingStatus feedItemProcessingStatus);
}