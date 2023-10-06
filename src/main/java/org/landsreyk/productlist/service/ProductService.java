package org.landsreyk.productlist.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.landsreyk.productlist.dto.ProductListBinding;
import org.landsreyk.productlist.model.Product;
import org.landsreyk.productlist.model.ProductList;
import org.landsreyk.productlist.repository.ProductListRepository;
import org.landsreyk.productlist.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductListRepository productListRepository;
    private long currentId;

    @PostConstruct
    private void init() {
        currentId = this.productRepository.findAll().stream().map(Product::getId).max(Long::compare).orElse(0L) + 1;
    }

    public List<Product> retrieveAll() {
        return productRepository.findAll();
    }

    public Status save(Product product) {
        if (productRepository.exists(product.getId())) {
            return Status.ALREADY_EXISTS;
        }
        try {
            if (product.getId() == null) {
                product.setId(currentId++);
            }
            productRepository.save(product);
        } catch (Exception e) {
            log.error("invalid input, object invalid", e);
            return Status.INVALID_PRODUCT;
        }
        return Status.OK;
    }

    public Status add(ProductListBinding binding) {
        Optional<Product> optionalProduct = productRepository.findById(binding.getId());
        if (optionalProduct.isEmpty()) {
            return Status.PRODUCT_NOT_FOUND;
        }
        Product product = optionalProduct.get();
        Optional<ProductList> optionalProductList = productListRepository.findById(binding.getListId());
        if (optionalProductList.isEmpty()) {
            return Status.LIST_NOT_FOUND;
        } else if (product.getListId() != null && Objects.equals(product.getListId(), binding.getListId())) {
            return Status.ALREADY_IN_LIST;
        }
        product.setListId(binding.getListId());
        productRepository.save(product);
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
