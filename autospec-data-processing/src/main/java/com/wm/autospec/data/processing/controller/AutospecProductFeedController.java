package com.wm.autospec.data.processing.controller;

import com.wm.autospec.data.processing.model.ProductFeedItem;
import com.wm.autospec.data.processing.service.AutoSpecProductFeedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autospec")
@Slf4j
public class AutospecProductFeedController {

    @Autowired
    AutoSpecProductFeedService autoSpecProductFeedService;

    @PostMapping("/api/v1/feeds/{feedId}")
    public ResponseEntity<String> saveProductFeedItem(@RequestBody ProductFeedItem productFeedItem) {

        log.info("Controller Received ProductFeedItem: {}", productFeedItem);
        try {
            autoSpecProductFeedService.saveProductFeedItem(productFeedItem);
            log.info("Controller Saved ProductFeedItem with id: {}", productFeedItem.getId());
            return ResponseEntity.ok("ProductFeedItem saved successfully");
        } catch (Exception e) {
            log.error("Error saving ProductFeedItem: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Failed to save ProductFeedItem");
        }
    }

    @GetMapping("/api/v1/feeds/{feedId}")
    public List<ProductFeedItem> getFeedItemDetailsForFeed(@PathVariable("feedId") String feedId) {

        log.info("Controller Received feedId: {}", feedId);
        List<ProductFeedItem> productFeedItems = autoSpecProductFeedService.getProductFeedDetails(feedId);
        log.info("Controller Processed feedId with size : {}", CollectionUtils.isEmpty(productFeedItems) ? 0 : productFeedItems.size());

        return productFeedItems;
    }
}