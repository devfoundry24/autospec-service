package com.wm.domain.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Map;

@Data
@Document(collection = "product_feed_items") // Use actual collection name
public class ProductFeedItem {

    @Id
    private String id;

    @Field("feedId")
    private String feedId;

    @Field("feed_item_status")
    private String feedItemStatus;

    @Field("feed_item_raw_text")
    private String feedItemRawText;

    @Field("feed_item_submission_datetime")
    private Instant feedItemSubmissionDatetime;

    @Field("feed_item_completion_datetime")
    private Instant feedItemCompletionDatetime;

    @Field("failure_reason")
    private String failureReason;

    @Field("product_type_key")
    private String productTypeKey;

    @Field("product_type_version")
    private String productTypeVersion;

    @Field("product_type_confidence_score")
    private Double productTypeConfidenceScore;

    @Field("feed_item_structured_data")
    private Map<String, Object> feedItemStructuredData;

    @Field("feed_item_additional_data")
    private Map<String, Object> feedItemAdditionalData;
}

