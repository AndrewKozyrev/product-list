package org.landsreyk.productlist.service;

import lombok.extern.slf4j.Slf4j;
import org.landsreyk.productlist.dto.ProductListBinding;
import org.landsreyk.productlist.model.Product;
import org.landsreyk.productlist.model.ProductList;
import org.landsreyk.productlist.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {
    protected ProductRepository repo;
    @Autowired
    @Lazy
    private ProductListService productListService;
    protected long currentId;

    @Autowired
    public ProductService(ProductRepository repository) {
        this.repo = repository;
        currentId = repo.findAll().stream().map(Product::getId).max(Long::compare).orElse(0L) + 1;
    }

    public List<Product> retrieveAll() {
        return repo.findAll();
    }

    public Status save(Product product) {
        if (repo.exists(product.getId())) {
            return Status.ALREADY_EXISTS;
        }
        try {
            if (product.getId() == null) {
                product.setId(currentId++);
            }
            repo.save(product);
        } catch (Exception e) {
            log.error("invalid input, object invalid", e);
            return Status.INVALID_PRODUCT;
        }
        return Status.OK;
    }

    public Collection<Product> retrieveByListId(Long listId) {
        return repo.findByListId(listId);
    }

    public Optional<Product> retrieveById(Long id) {
        return repo.findById(id);
    }

    public Status add(ProductListBinding binding) {
        Optional<Product> optionalProduct = retrieveById(binding.getId());
        if (optionalProduct.isEmpty()) {
            return Status.PRODUCT_NOT_FOUND;
        }
        Product product = optionalProduct.get();
        Optional<ProductList> optionalProductList = productListService.retrieveById(binding.getListId());
        if (optionalProductList.isEmpty()) {
            return Status.LIST_NOT_FOUND;
        } else if (product.getListId() != null && Objects.equals(product.getListId(), binding.getListId())) {
            return Status.ALREADY_IN_LIST;
        }
        product.setListId(binding.getListId());
        repo.save(product);
        return Status.OK;
    }

    public enum Status {
        INVALID_PRODUCT,
        ALREADY_EXISTS,
        OK,
        PRODUCT_NOT_FOUND,
        ALREADY_IN_LIST,
        LIST_NOT_FOUND
    }
}
