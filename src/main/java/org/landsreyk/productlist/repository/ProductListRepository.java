package org.landsreyk.productlist.repository;

import org.landsreyk.productlist.model.ProductList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductListRepository extends MongoRepository<ProductList, Long> {
    Optional<ProductList> findById(Long id);

    default boolean exists(Long id) {
        return id != null && findById(id).isPresent();
    }
}
