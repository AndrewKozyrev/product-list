package org.landsreyk.productlist.repository;

import org.landsreyk.productlist.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, Long> {

    Optional<Product> findById(Long id);
    List<Product> findByName(String name);
    List<Product> findByListId(Long listId);
    default boolean exists(Long id) {
        return id != null && findById(id).isPresent();
    }
}
