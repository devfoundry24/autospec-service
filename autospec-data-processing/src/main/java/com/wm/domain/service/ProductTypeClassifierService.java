package com.wm.domain.service;

import com.wm.domain.model.FeedItemProcessingStatus;
import com.wm.domain.model.ProductFeedItem;
import com.wm.domain.port.in.ProductTypeClassificationUseCase;
import com.wm.domain.port.out.LLMClientPort;
import com.wm.domain.port.out.ProductFeedItemRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ProductTypeClassifierService implements ProductTypeClassificationUseCase {

    private final ProductFeedItemRepositoryPort repositoryPort;
    private final LLMClientPort llmClientPort;

    @Override
    public void classifyProductType(String feedItemId) {

        ProductFeedItem item = repositoryPort.findByFeedItemId(feedItemId);
        String prompt = "Classify product: " + item.getFeedItemRawText();

        String classification = llmClientPort.getProductTypeFromLLM(prompt);

        log.info("Product Type Classification for feedItemId {}: {}", feedItemId, classification);

        item.setProductTypeKey(classification);
        item.setFeedItemStatus(String.valueOf(FeedItemProcessingStatus.PT_CLASSIFICATION_COMPLETED));

        repositoryPort.saveFeedItem(item);
    }

}
