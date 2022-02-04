package org.landsreyk.productlist.controller;

import org.landsreyk.productlist.model.Product;
import org.landsreyk.productlist.model.ProductBinding;
import org.landsreyk.productlist.service.PListService;
import org.landsreyk.productlist.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ProductController {

    private final ProductService productService;
    private final PListService pListService;

    public ProductController(ProductService productService, PListService pListService) {
        this.productService = productService;
        this.pListService = pListService;
    }

    @GetMapping("/products")
    public ResponseEntity<?> getProducts() {
        List<Product> list =  productService.retrieveALl();
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/products")
    public ResponseEntity<?> saveProduct(@RequestBody Product product) {
        int statusCode = productService.save(product);
        return switch (statusCode) {
            case 201 -> ResponseEntity.status(201).body(null);
            case 409 -> ResponseEntity.status(409).body(Map.of("error", "продукт уже существует."));
            case 400 -> ResponseEntity.status(400).body(Map.of("error", "невозможно сохранить объект."));
            default -> ResponseEntity.status(statusCode).body(null);
        };
    }

    @PutMapping("/add")
    public ResponseEntity<?> addToList(@RequestBody ProductBinding productBinding) {
        Optional<Product> optionalProduct = productService.retrieveById(productBinding.getId());
        int statusCode;
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            statusCode = pListService.add(product, productBinding.getListId());
            productService.update(product);
        }
        else {
            statusCode = 470;
        }
        return switch (statusCode) {
            case 200 -> ResponseEntity.status(statusCode).body(null);
            case 470 -> ResponseEntity.status(statusCode).body(Map.of("error", "продукт с таким id не существует."));
            case 471 -> ResponseEntity.status(statusCode).body(Map.of("error", "список с таким id не существует."));
            default -> ResponseEntity.status(statusCode).body(null);
        };
    }
}
