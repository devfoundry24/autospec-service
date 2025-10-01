package com.wm.adapter.in.web.mapper;

import com.wm.adapter.in.web.dto.ProductFeedItemRequestDTO;
import com.wm.domain.model.ProductFeedItem;

import java.util.List;

public class ProductFeedItemRequestMapper {

    /**
     * Converts a ProductFeedItemRequestDTO to a ProductFeedItem domain object.
     *
     * @param dto the ProductFeedItemRequestDTO to convert
     * @return the converted ProductFeedItem, or null if the input DTO is null
     */
    public static ProductFeedItem toDomain(ProductFeedItemRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        ProductFeedItem item = new ProductFeedItem();
        item.setId(dto.getFeedItemId());
        item.setFeedItemRawText(dto.getProductDescription());
        item.setFeedItemImageData(
                dto.getProductImageDataList() != null ? dto.getProductImageDataList() : List.of()
        );
        //If time is null then pass current time
        item.setFeedItemCreationTime(dto.getTimeStamp() != null ? dto.getTimeStamp(): java.time.Instant.now());
        return item;
    }
}
