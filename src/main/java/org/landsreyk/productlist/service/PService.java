package org.landsreyk.productlist.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.landsreyk.productlist.dto.ProductListBinding;
import org.landsreyk.productlist.model.PList;
import org.landsreyk.productlist.model.Product;
import org.landsreyk.productlist.repository.PRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PService {
    protected final Logger logger = LogManager.getLogger(PService.class);
    protected final PRepository repo;
    protected long currentId;

    public PService(PRepository repo) {
        this.repo = repo;
        currentId = repo.findAll().stream().map(Product::getId).max(Long::compare).orElse(0L) + 1;
    }

    public List<Product> retrieveALl() {
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
            logger.fatal("invalid input, object invalid", e);
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

    public Status add(ProductListBinding binding, PListService pListService) {
        Optional<Product> optionalProduct = retrieveById(binding.getId());
        if (optionalProduct.isEmpty()) {
            return Status.PRODUCT_NOT_FOUND;
        }
        Product product = optionalProduct.get();
        Optional<PList> optionalProductList = pListService.retrieveById(binding.getListId());
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
