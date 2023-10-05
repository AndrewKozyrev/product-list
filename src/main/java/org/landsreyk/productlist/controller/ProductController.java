package org.landsreyk.productlist.controller;

import org.landsreyk.productlist.dto.ProductListBinding;
import org.landsreyk.productlist.model.Product;
import org.landsreyk.productlist.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return productService.retrieveAll();
    }

    @PostMapping("/products")
    public ProductService.Status saveProduct(@RequestBody Product product) {
        return productService.save(product);
    }

    @PutMapping("/add")
    public ProductService.Status addToList(@RequestBody ProductListBinding productListBinding) {
        return productService.add(productListBinding);
    }
}
