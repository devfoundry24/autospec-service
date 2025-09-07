package com.wm.adapter.in.web.mapper;

import com.wm.adapter.in.web.dto.ProductFeedItemResponseDTO;
import com.wm.domain.model.ProductFeedItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductFeedItemResponseMapper {

    @Mapping(source = "id", target = "feedItemId")
    @Mapping(source = "feedItemStatus", target = "status")
    @Mapping(source = "feedItemRawText", target = "productDescription")
    @Mapping(source = "feedItemCreationTime", target = "creationTime")
    @Mapping(source = "feedItemModificationTime", target = "modificationTime")
    ProductFeedItemResponseDTO toDTO(ProductFeedItem domain);
}