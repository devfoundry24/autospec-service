package com.wm.adapter.in.web.dto;

import lombok.Data;

import java.time.Instant;
import java.util.List;


@Data
public class ProductFeedItemRequestDTO {
    private String feedItemId;
    private String productDescription;
    private Instant timeStamp;
    private List<String> productImageDataList;
}
