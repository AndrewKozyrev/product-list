package org.landsreyk.productlist.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document("products")
public class Product {

    @Id
    private Long id;
    private String name;
    private String description;
    private int kcal;
    @Field("list_id")
    private Long listId;

    @JsonProperty("list_id")
    public Long getListId() {
        return listId;
    }
}