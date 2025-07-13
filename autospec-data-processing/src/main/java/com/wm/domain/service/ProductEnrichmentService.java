package com.wm.domain.service;

import com.wm.domain.model.FeedItemProcessingStatus;
import com.wm.domain.model.ProductFeedItem;
import com.wm.domain.port.in.ProductEnrichmentUseCase;
import com.wm.domain.port.out.LLMClientPort;
import com.wm.domain.port.out.ProductFeedItemRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class ProductEnrichmentService implements ProductEnrichmentUseCase {

    private final ProductFeedItemRepositoryPort repositoryPort;
    private final LLMClientPort llmClientPort;

    @Override
    public void classifyProductType(String feedItemId) {
        ProductFeedItem item = repositoryPort.findByFeedItemId(feedItemId);
        if (item == null) {
            log.warn("Feed item with ID {} not found", feedItemId);
            return;
        }

        String productType = llmClientPort.getProductTypeFromLLM("Classifying product: " + item.getFeedItemRawText());
        log.info("Product Type Classification for feedItemId {}: {}", feedItemId, productType);

        if (productType == null || productType.isEmpty()) {
            updateItemWithFailure(item, "[Unknown]", FeedItemProcessingStatus.PT_CLASSIFICATION_FAILED, "No product type extracted");
        } else {
            updateItemWithSuccess(item, productType, FeedItemProcessingStatus.PT_CLASSIFICATION_COMPLETED);
        }
        repositoryPort.saveFeedItem(item);
    }

    @Override
    public void extractProductAttributes(String feedItemId) {
        ProductFeedItem item = repositoryPort.findByFeedItemId(feedItemId);
        if (item == null) {
            log.warn("Feed item with ID {} not found", feedItemId);
            return;
        }

        String productDescription = item.getFeedItemRawText() != null
                ? "Extracting attributes for product: " + item.getFeedItemRawText()
                : "Extracting attributes for product: [No description available]";
        String productType = item.getProductType() != null ? item.getProductType() : "[Unknown]";

        Map<String, Object> attributes = llmClientPort.getProductAttributesFromLLM(productDescription, productType);
        if (attributes == null || attributes.isEmpty()) {
            updateItemWithFailure(item, Map.of("error", "No attributes extracted"), FeedItemProcessingStatus.ATTRIBUTE_EXTRACTION_FAILED, "No product attributes extracted");
        } else {
            updateItemWithAttributes(item, attributes);
        }
        repositoryPort.saveFeedItem(item);
    }

    /**
     * Updates the given ProductFeedItem with failure details.
     *
     * @param item   The ProductFeedItem to update.
     * @param value  The value to set, which can be a Map for attributes or a String for product type.
     * @param status The processing status to set for the item.
     * @param reason The reason for the failure to set in the item.
     */
    private void updateItemWithFailure(ProductFeedItem item, Object value, FeedItemProcessingStatus status, String reason) {
        item.setProductAttributes(value instanceof Map ? (Map<String, Object>) value : null);
        item.setProductType(value instanceof String ? (String) value : null);
        item.setFeedItemModificationTime(Instant.now());
        item.setFeedItemStatus(status.toString());
        item.setFailureReason(reason);
    }

    /**
     * Updates the given ProductFeedItem with success details for product type classification.
     *
     * @param item        The ProductFeedItem to update.
     * @param productType The classified product type to set in the item.
     * @param status      The processing status to set for the item.
     */
    private void updateItemWithSuccess(ProductFeedItem item, String productType, FeedItemProcessingStatus status) {
        item.setProductType(productType);
        item.setFeedItemModificationTime(Instant.now());
        item.setFeedItemStatus(status.toString());
    }

    /**
     * Updates the given ProductFeedItem with extracted attributes and confidence score.
     *
     * @param item       The ProductFeedItem to update.
     * @param attributes A map containing the extracted attributes and confidence score.
     */
    private void updateItemWithAttributes(ProductFeedItem item, Map<String, Object> attributes) {
        item.setFeedItemModificationTime(Instant.now());
        item.setProductAttributes((Map<String, Object>) attributes.getOrDefault("productAttributes", Map.of()));
        item.setConfidenceScore((Double) attributes.getOrDefault("confidenceScore", 0.0));
        item.setFeedItemStatus(FeedItemProcessingStatus.ATTRIBUTE_EXTRACTION_COMPLETED.toString());
    }

}