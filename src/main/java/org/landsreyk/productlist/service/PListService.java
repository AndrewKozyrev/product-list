package org.landsreyk.productlist.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.landsreyk.productlist.model.Product;
import org.landsreyk.productlist.model.PList;
import org.landsreyk.productlist.repository.PListRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PListService {

    protected final Logger logger = LogManager.getLogger(PListService.class);
    protected final PListRepository repo;
    protected long currentId = 1;

    public PListService(PListRepository repo) {
        this.repo = repo;
    }

    public List<PList> retrieveALl() {
        return repo.findAll();
    }

    public int save(PList list) {
        if (repo.exists(list.getId())) {
            return 409;
        }
        try {
            if (list.getId() == null) {
                list.setId(currentId++);
            }
            repo.save(list);
        } catch (Exception e) {
            logger.fatal("invalid input, object invalid", e);
            return 400;
        }
        return 201;
    }

    public int add(Product product, Long listId) {
        Optional<PList> optionalProductList = repo.findById(listId);
        if (optionalProductList.isEmpty()) {
            return 471;
        }
        else if (product.getListId() != null && Objects.equals(product.getListId(), listId)) {
            return 409;
        }
        product.setListId(listId);
        return 200;
    }

    public Map<PList, Collection<Product>> mapToProducts(ProductService productService) {
        List<PList> productLists = retrieveALl();
        Map<PList, Collection<Product>> map = new HashMap<>();
        for (PList p : productLists) {
            Collection<Product> products = productService.retrieveByListId(p.getId());
            map.put(p, products);
        }
        return map;
    }
}
