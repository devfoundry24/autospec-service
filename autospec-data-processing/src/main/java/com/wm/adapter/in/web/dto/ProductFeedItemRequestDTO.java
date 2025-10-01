package com.wm.adapter.in.web.dto;

import lombok.Data;

import java.time.Instant;


@Data
public class ProductFeedItemRequestDTO {
    private String feedItemId;
    private String productDescription;
    private Instant timeStamp;
    private String productImageData;
}
