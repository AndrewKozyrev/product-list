package org.landsreyk.productlist.repository;

import org.landsreyk.productlist.model.PList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PListRepository extends MongoRepository<PList, Long> {
    Optional<PList> findById(Long id);
    List<PList> findByName(String name);
    default boolean exists(Long id) {
        return id != null && findById(id).isPresent();
    }
}
