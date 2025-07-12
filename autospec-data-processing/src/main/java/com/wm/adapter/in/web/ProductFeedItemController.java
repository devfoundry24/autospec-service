package com.wm.adapter.in.web;

import com.wm.domain.model.ProductFeedItem;
import com.wm.domain.port.in.ProductFeedItemUseCase;
import com.wm.domain.port.in.ProductTypeClassificationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autospec/api/v1/feedItem")
@Slf4j
@RequiredArgsConstructor
public class ProductFeedItemController {

    private final ProductFeedItemUseCase productFeedItemUseCase;
    private final ProductTypeClassificationUseCase productTypeClassificationUseCase;

    /**
     * Endpoint to save a ProductFeedItem
     * @param feedItemId
     * @param productFeedItem
     * @return
     */
    @PostMapping("/{feedItemId}")
    public ResponseEntity<String> saveProductFeedItem(@PathVariable("feedItemId") String feedItemId, @RequestBody ProductFeedItem productFeedItem) {

        log.info("Controller Received ProductFeedItem: {}", productFeedItem);
        try {
            productFeedItemUseCase.saveFeedItem(productFeedItem);
            log.info("Controller Saved ProductFeedItem with id: {}", productFeedItem.getId());
            return ResponseEntity.ok("ProductFeedItem saved successfully");
        } catch (Exception e) {
            log.error("Error saving ProductFeedItem: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Failed to save ProductFeedItem");
        }
    }

    /**
     * Endpoint to get details of a ProductFeedItem by feedItemId
     * @param feedItemId
     * @return
     */
    @GetMapping("/{feedItemId}")
    public ResponseEntity<?> getProductFeedItem(@PathVariable("feedItemId") String feedItemId) {
        log.info("Controller Received feedId: {}", feedItemId);

        ProductFeedItem productFeedItem = productFeedItemUseCase.getProductFeedItem(feedItemId);

        if (productFeedItem == null) {
            String message = "No Product Feed Item found with Id: " + feedItemId;
            log.warn(message);
            return ResponseEntity.status(404).body(message);
        }

        return ResponseEntity.ok(productFeedItem);
    }

    @PostMapping("/{feedItemId}/process/ptclassify")
    public ResponseEntity<String> processProductFeedItemPTClassification(@PathVariable("feedItemId") String feedItemId) {
        try {
            productTypeClassificationUseCase.classifyProductType(feedItemId);
            log.info("Product Feed Item with id: {} classified successfully", feedItemId);
            return ResponseEntity.ok("Product Feed Item classified successfully");
        } catch (Exception e) {
            log.error("Error classifying Product Feed Item: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Failed to classify Product Feed Item");
        }
    }
}