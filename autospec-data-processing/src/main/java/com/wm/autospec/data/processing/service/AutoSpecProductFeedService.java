package com.wm.autospec.data.processing.service;

import com.wm.autospec.data.processing.model.ProductFeedItem;
import com.wm.autospec.data.processing.repository.ProductFeedItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AutoSpecProductFeedService {

    @Autowired
    private ProductFeedItemRepository feedItemRepository;



    public void saveProductFeedItem(ProductFeedItem productFeedItem) {
        log.info("Saving product feed item with id: {}", productFeedItem.getId());
        try {
            feedItemRepository.save(productFeedItem);
            log.info("Product feed item saved successfully with id: {}", productFeedItem.getId());
        } catch (Exception e) {
            log.error("Error saving product feed item: {}", e.getMessage(), e);
        }
    }

    public List<ProductFeedItem> getProductFeedDetails(String productFeedId) {

        log.info("Fetching product feed details for feedId: {}", productFeedId);

        List<ProductFeedItem> feedItems = feedItemRepository.findByFeedId(productFeedId);

        if (feedItems.isEmpty()) {
            log.warn("No feed item found for feedId: {}", productFeedId);
            return new ArrayList<ProductFeedItem>();
        }

        log.info("Feed item found with size : {}", feedItems.size());

        return feedItems;
    }
}