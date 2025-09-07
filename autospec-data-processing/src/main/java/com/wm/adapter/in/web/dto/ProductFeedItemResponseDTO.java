package com.wm.adapter.in.web.dto;

import lombok.Data;
import java.time.Instant;
import java.util.Map;

@Data
public class ProductFeedItemResponseDTO {
    private String feedItemId;
    private String productDescription;
    private String status;
    private String productType;
    private Double confidenceScore;
    private Map<String, Object> productAttributes;
    private Instant creationTime;
    private Instant modificationTime;
}
