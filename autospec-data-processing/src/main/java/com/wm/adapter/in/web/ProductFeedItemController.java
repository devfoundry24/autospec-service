package com.wm.adapter.in.web;

import com.wm.adapter.in.web.dto.ProductFeedItemRequestDTO;
import com.wm.adapter.in.web.dto.ProductFeedItemResponseDTO;
import com.wm.adapter.in.web.mapper.ProductFeedItemRequestMapper;
import com.wm.adapter.in.web.mapper.ProductFeedItemResponseMapper;
import com.wm.domain.model.ProductFeedItem;
import com.wm.domain.port.in.ProductFeedItemUseCase;
import com.wm.domain.port.in.ProductEnrichmentUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autospec/api/v1/feedItem")
@Slf4j
@RequiredArgsConstructor
public class ProductFeedItemController {

    private final ProductFeedItemUseCase productFeedItemUseCase;
    private final ProductEnrichmentUseCase productEnrichmentUseCase;
    private final ProductFeedItemResponseMapper productFeedItemResponseMapper;



    /**
     * Endpoint to save a ProductFeedItem
     * @param feedItemId
     * @param productFeedItemDto
     * @return
     */
    @PostMapping("/{feedItemId}")
    public ResponseEntity<String> saveProductFeedItem(@PathVariable("feedItemId") String feedItemId, @RequestBody ProductFeedItemRequestDTO productFeedItemDto) {

        log.info("Controller Received ProductFeedItem: {}", productFeedItemDto);
        try {
            //Manual Mapper
            ProductFeedItem productFeedItem = ProductFeedItemRequestMapper.toDomain(productFeedItemDto);
            productFeedItemUseCase.saveFeedItem(productFeedItem);
            log.info("Controller Saved ProductFeedItem with id: {}", productFeedItem.getId());
            return ResponseEntity.ok("ProductFeedItem : "+feedItemId+" saved successfully ");
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
        //Automated Mapper
        ProductFeedItemResponseDTO productFeedItemDto = productFeedItemResponseMapper.toDTO(productFeedItem);
        return ResponseEntity.ok(productFeedItemDto);
    }

    /**
     *
     * @param feedItemId
     * @return
     */
    @PostMapping("/{feedItemId}/classify")
    public ResponseEntity<String> processProductFeedItemPTClassification(@PathVariable("feedItemId") String feedItemId) {
        try {
            productEnrichmentUseCase.classifyProductType(feedItemId);
            log.info("Product Feed Item with id: {} classified successfully", feedItemId);
            return ResponseEntity.ok("Product Feed Item with id: " + feedItemId + " classified successfully");
        } catch (Exception e) {
            log.error("Error classifying Product Feed Item: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Failed to classify Product Feed Item");
        }
    }

    /**
     * Endpoint to extract attributes from a ProductFeedItem by feedItemId
     * @param feedItemId
     * @return
     */
    @PostMapping("/{feedItemId}/extract")
    public ResponseEntity<String> processProductFeedItemAttributeExtraction(@PathVariable("feedItemId") String feedItemId) {
        try {
            productEnrichmentUseCase.extractProductAttributes(feedItemId);
            return ResponseEntity.ok("Product Feed Item with id: " + feedItemId + " attributes extracted successfully");
        } catch (Exception e) {
            log.error("Error extracting attributes from Product Feed Item: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Failed to extract attributes from Product Feed Item");
        }
    }

    /**
     * Batch processing endpoints for ProductFeedItem classification
     */
    @PostMapping("/classify/batch")
    public ResponseEntity<?> processProductFeedItemPTClassificationBatch(@RequestBody List<String> feedItemIds) {
        try {
            productEnrichmentUseCase.classifyProductTypeBatch(feedItemIds);
            return ResponseEntity.ok("Batch classification completed successfully");
        } catch (Exception e) {
            log.error("Error during batch classification: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Batch classification failed");
        }
    }

    /**
     * Batch processing endpoint for ProductFeedItem attribute extraction
     * @param feedItemIds
     * @return
     */
    @PostMapping("/extract/batch")
    public ResponseEntity<?> processProductFeedItemAttributeExtractionBatch(@RequestBody List<String> feedItemIds) {
        try {
            productEnrichmentUseCase.extractProductAttributesBatch(feedItemIds);
            return ResponseEntity.ok("Batch Attribute extraction completed successfully");
        } catch (Exception e) {
            log.error("Error during batch attribute extraction: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Batch classification failed");
        }
    }

}