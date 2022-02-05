package org.landsreyk.productlist.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.landsreyk.productlist.model.Product;
import org.landsreyk.productlist.model.ProductBinding;
import org.landsreyk.productlist.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    protected final Logger logger = LogManager.getLogger(ProductService.class);
    protected final ProductRepository repo;
    protected long currentId = 1;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> retrieveALl() {
        return repo.findAll();
    }

    public int save(Product product) {
        if (repo.exists(product.getId())) {
            return 409;
        }
        try {
            if (product.getId() == null) {
                product.setId(currentId++);
            }
            repo.save(product);
        } catch (Exception e) {
            logger.fatal("invalid input, object invalid", e);
            return 400;
        }
        return 201;
    }

    public void update(Product product) {
        repo.save(product);
    }

    public Collection<Product> retrieveByListId(Long listId) {
        return repo.findByListId(listId);
    }

    public Optional<Product> retrieveById(Long id) {
        return repo.findById(id);
    }
}
