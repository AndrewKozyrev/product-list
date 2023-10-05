package org.landsreyk.productlist.service;

import lombok.extern.slf4j.Slf4j;
import org.landsreyk.productlist.dto.ListData;
import org.landsreyk.productlist.model.Product;
import org.landsreyk.productlist.model.ProductList;
import org.landsreyk.productlist.repository.ProductListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductListService {
    protected final ProductListRepository repo;

    private final ProductService productService;
    protected long currentId;

    @Autowired
    public ProductListService(ProductListRepository repo, ProductService productService) {
        this.repo = repo;
        currentId = repo.findAll().stream().map(ProductList::getId).max(Long::compare).orElse(0L) + 1;
        this.productService = productService;
    }

    public List<ProductList> retrieveAll() {
        return repo.findAll();
    }

    public Status save(ProductList list) {
        if (repo.exists(list.getId())) {
            return Status.ALREADY_EXISTS;
        }
        try {
            if (list.getId() == null) {
                list.setId(currentId++);
            }
            repo.save(list);
        } catch (Exception e) {
            log.error("invalid input, object invalid", e);
            return Status.INVALID_LIST;
        }
        return Status.OK;
    }

    public List<ListData> retrieveLists() {
        List<ListData> list = new ArrayList<>();
        List<ProductList> productLists = retrieveAll();
        for (ProductList p : productLists) {
            Collection<Product> products = productService.retrieveByListId(p.getId());
            ListData item = new ListData();
            item.setList(p);
            item.setProducts(products);
            long totalKcal = products.stream()
                    .map(Product::getKcal)
                    .reduce(Integer::sum)
                    .orElse(0);
            item.setTotalKcal(totalKcal);
            list.add(item);
        }
        return list;
    }

    public Optional<ProductList> retrieveById(Long listId) {
        return repo.findById(listId);
    }

    public enum Status {
        INVALID_LIST,
        ALREADY_EXISTS,
        OK
    }
}
