package com.wm.domain.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Map;

@Data
@Document(collection = "product_feed_items") // Use actual collection name
public class ProductFeedItem {

    @Id
    private String id;

    @Field("feed_item_status")
    private String feedItemStatus;

    @Field("feed_item_raw_text")
    private String feedItemRawText;

    @Field("feed_item_image_data")
    private String feedItemImageData;

    @Field("feed_item_modification_datetime")
    private Instant feedItemCreationTime;

    @Field("feed_item_update_datetime")
    private Instant feedItemModificationTime;

    @Field("failure_reason")
    private String failureReason;

    @Field("product_type")
    private String productType;

    @Field("confidence_score")
    private Double confidenceScore;

    @Field("product_attributes")
    private Map<String, Object> productAttributes;

}

