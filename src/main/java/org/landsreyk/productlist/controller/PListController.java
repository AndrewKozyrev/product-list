package org.landsreyk.productlist.controller;

import org.landsreyk.productlist.dto.ListData;
import org.landsreyk.productlist.model.PList;
import org.landsreyk.productlist.service.PListService;
import org.landsreyk.productlist.service.PService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PListController {

    private final PListService pListService;
    private final PService pService;

    public PListController(PListService pListService, PService pService) {
        this.pListService = pListService;
        this.pService = pService;
    }

    @GetMapping("/lists")
    public List<ListData> getLists() {
        return pListService.retrieveLists(pService);
    }

    @PostMapping("/lists")
    public PListService.Status saveProduct(@RequestBody PList list) {
        return pListService.save(list);
    }


}
