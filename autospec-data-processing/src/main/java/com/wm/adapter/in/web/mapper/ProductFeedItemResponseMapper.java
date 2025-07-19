package com.wm.adapter.in.web.mapper;

import com.wm.adapter.in.web.dto.ProductFeedItemResponseDTO;
import com.wm.domain.model.ProductFeedItem;

public class ProductFeedItemResponseMapper {

    /**
     * Converts a ProductFeedItem domain object to a ProductFeedItemResponseDTO.
     *
     * @param productFeedItem the ProductFeedItem to convert
     * @return the converted ProductFeedItemResponseDTO, or null if the input is null
     */
    public static ProductFeedItemResponseDTO toDto(ProductFeedItem productFeedItem) {
        if (productFeedItem == null) {
            return null;
        }
        return new ProductFeedItemResponseDTO();
    }
}