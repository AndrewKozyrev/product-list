package org.landsreyk.productlist.controller;

import org.landsreyk.productlist.dto.ProductListBinding;
import org.landsreyk.productlist.model.Product;
import org.landsreyk.productlist.service.PListService;
import org.landsreyk.productlist.service.PService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PController {

    private final PService pService;
    private final PListService pListService;

    public PController(PService pService, PListService pListService) {
        this.pService = pService;
        this.pListService = pListService;
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return pService.retrieveALl();
    }

    @PostMapping("/products")
    public PService.Status saveProduct(@RequestBody Product product) {
        return pService.save(product);
    }

    @PutMapping("/add")
    public PService.Status addToList(@RequestBody ProductListBinding productListBinding) {
        return pService.add(productListBinding, pListService);
    }
}
