package org.landsreyk.productlist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.landsreyk.productlist.model.PList;
import org.landsreyk.productlist.model.Product;

import java.util.Collection;

@Data
public class ListData {
    private PList list;
    private Collection<Product> products;
    private long totalKcal;

    @JsonProperty("total_kcal")
    public long getTotalKcal() {
        return totalKcal;
    }
}
