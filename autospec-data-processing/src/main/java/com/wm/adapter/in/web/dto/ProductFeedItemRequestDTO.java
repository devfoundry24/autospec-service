package com.wm.adapter.in.web.dto;

import lombok.Data;

import java.time.Instant;


@Data
public class ProductFeedItemRequestDTO {
    private String id;
    private String productDescription;
    private String productImageUrl;
    private Instant timeStamp;
}
