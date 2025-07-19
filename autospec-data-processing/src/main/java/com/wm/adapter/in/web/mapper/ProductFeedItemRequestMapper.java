package com.wm.adapter.in.web.mapper;

import com.wm.adapter.in.web.dto.ProductFeedItemRequestDTO;
import com.wm.domain.model.ProductFeedItem;

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
        item.setId(dto.getId());
        item.setFeedItemRawText(dto.getProductDescription());
        item.setFeedItemCreationTime(dto.getTimeStamp());
        return item;
    }
}
