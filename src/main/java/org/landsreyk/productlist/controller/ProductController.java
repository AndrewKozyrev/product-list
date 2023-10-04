package org.landsreyk.productlist.controller;

import org.landsreyk.productlist.dto.ProductListBinding;
import org.landsreyk.productlist.model.Product;
import org.landsreyk.productlist.service.ProductListService;
import org.landsreyk.productlist.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;
    private final ProductListService productListService;

    public ProductController(ProductService productService, ProductListService productListService) {
        this.productService = productService;
        this.productListService = productListService;
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return productService.retrieveALl();
    }

    @PostMapping("/products")
    public ProductService.Status saveProduct(@RequestBody Product product) {
        return productService.save(product);
    }

    @PutMapping("/add")
    public ProductService.Status addToList(@RequestBody ProductListBinding productListBinding) {
        return productService.add(productListBinding, productListService);
    }
}
