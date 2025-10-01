package com.wm.adapter.in.cron;

import com.wm.domain.port.in.ProductEnrichmentUseCase;
import com.wm.domain.port.out.ProductFeedItemRepositoryPort;
import com.wm.domain.model.ProductFeedItem;
import com.wm.domain.model.FeedItemProcessingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductEnrichmentCron {

    private final ProductEnrichmentUseCase productEnrichmentUseCase;
    private final ProductFeedItemRepositoryPort feedItemRepository;

    /**
     * Scheduled task that runs every minute to classify and extract attributes
     * for product feed items with the status "ACCEPTED".
     **/
    @Scheduled(cron = "0/30 * * * * ?") // Every 30 seconds
    public void runClassificationAndExtraction() {
        log.info("🚀 Starting Cron Job: Classification + Attribute Extraction");

        // Fetch all feed items with status "ACCEPTED"
        List<ProductFeedItem> items = feedItemRepository.findByFeedItemStatus(FeedItemProcessingStatus.ACCEPTED);
        //If no items found, log and exit
        if (CollectionUtils.isEmpty(items)) {
            log.warn("No Product Feed Items found with status ACCEPTED. Exiting Cron Job.");
            return;
        }

        for (ProductFeedItem item : items) {
            String id = item.getId();
            try {
                if(item.getFeedItemImageData() == null || item.getFeedItemImageData().isEmpty()) {
                    log.info("➡️ Classifying ProductType for feedId: {}", id);
                    productEnrichmentUseCase.classifyProductType(id);

                    log.info("➡️ Extracting Attributes for feedId: {}", id);
                    productEnrichmentUseCase.extractProductAttributes(id);

                    log.info("✅ Done processing feedId: {}", id);
                }else{
                    log.info("➡️ Classifying ProductType from Image for feedId: {}", id);
                    productEnrichmentUseCase.classifyProductTypeFromImage(id);

                    log.info("➡️ Extracting Attributes from Image for feedId: {}", id);
                    productEnrichmentUseCase.extractProductAttributesFromImage(id);

                    log.info("✅ Done processing feedId: {}", id);
                }
            } catch (Exception e) {
                log.error("❌ Error processing feedId {}: {}", id, e.getMessage(), e);
            }
        }
        log.info("✅ Cron Job Completed");
    }
}
