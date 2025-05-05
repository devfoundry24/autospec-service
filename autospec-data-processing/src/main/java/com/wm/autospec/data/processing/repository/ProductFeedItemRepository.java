package com.wm.autospec.data.processing.repository;

import com.wm.autospec.data.processing.model.ProductFeedItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductFeedItemRepository extends MongoRepository<ProductFeedItem, String> {

    List<ProductFeedItem> findByFeedId(String feedId);

}