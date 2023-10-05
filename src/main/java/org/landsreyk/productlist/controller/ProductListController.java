package org.landsreyk.productlist.controller;

import org.landsreyk.productlist.dto.ListData;
import org.landsreyk.productlist.model.ProductList;
import org.landsreyk.productlist.service.ProductListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductListController {

    private final ProductListService productListService;

    @Autowired
    public ProductListController(ProductListService productListService) {
        this.productListService = productListService;
    }

    @GetMapping("/lists")
    public List<ListData> getLists() {
        return productListService.retrieveLists();
    }

    @PostMapping("/lists")
    public ProductListService.Status saveProduct(@RequestBody ProductList list) {
        return productListService.save(list);
    }


}
