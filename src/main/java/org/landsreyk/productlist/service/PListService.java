package org.landsreyk.productlist.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.landsreyk.productlist.dto.ListData;
import org.landsreyk.productlist.model.Product;
import org.landsreyk.productlist.model.PList;
import org.landsreyk.productlist.repository.PListRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PListService {

    protected final Logger logger = LogManager.getLogger(PListService.class);
    protected final PListRepository repo;
    protected long currentId;

    public PListService(PListRepository repo) {
        this.repo = repo;
        currentId = repo.findAll().stream().map(PList::getId).max(Long::compare).orElse(0L) + 1;
    }

    public List<PList> retrieveALl() {
        return repo.findAll();
    }

    public Status save(PList list) {
        if (repo.exists(list.getId())) {
            return Status.ALREADY_EXISTS;
        }
        try {
            if (list.getId() == null) {
                list.setId(currentId++);
            }
            repo.save(list);
        } catch (Exception e) {
            logger.fatal("invalid input, object invalid", e);
            return Status.INVALID_LIST;
        }
        return Status.OK;
    }

    public List<ListData> retrieveLists(PService pService) {
        List<ListData> list = new ArrayList<>();
        List<PList> productLists = retrieveALl();
        for (PList p : productLists) {
            Collection<Product> products = pService.retrieveByListId(p.getId());
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

    public Optional<PList> retrieveById(Long listId) {
        return repo.findById(listId);
    }

    public enum Status {
        INVALID_LIST,
        ALREADY_EXISTS,
        OK
    }
}
