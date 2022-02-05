package org.landsreyk.productlist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.landsreyk.productlist.model.PList;
import org.landsreyk.productlist.model.Product;
import org.landsreyk.productlist.service.PListService;
import org.landsreyk.productlist.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;

@RestController
public class PListController {

    private final PListService pListService;
    private final ProductService productService;

    public PListController(PListService pListService, ProductService productService) {
        this.pListService = pListService;
        this.productService = productService;
    }

    @GetMapping("/lists")
    public ResponseEntity<?> getLists() {
        Map<PList, Collection<Product>> map = pListService.mapToProducts(productService);
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode root = mapper.createArrayNode();
        map.entrySet().forEach(e -> {
            ObjectNode node = mapper.valueToTree(e.getKey());
            ArrayNode node2 = mapper.valueToTree(e.getValue());
            node.putArray("products").addAll(node2);
            long totalKcal = e.getValue().stream()
                    .map(Product::getKcal)
                    .reduce(Integer::sum)
                    .orElse(0);
            node.put("total_kcal", totalKcal);
            root.add(node);
        });

        return ResponseEntity.ok().body(root);
    }

    @PostMapping("/lists")
    public ResponseEntity<?> saveProduct(@RequestBody PList list) {
        int statusCode = pListService.save(list);
        return switch (statusCode) {
            case 201 -> ResponseEntity.status(201).body(null);
            case 409 -> ResponseEntity.status(409).body(Map.of("error", "список уже существует."));
            case 400 -> ResponseEntity.status(400).body(Map.of("error", "невозможно сохранить список."));
            default -> ResponseEntity.status(statusCode).body(null);
        };
    }
}
