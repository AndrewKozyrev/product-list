package org.landsreyk.productlist.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "lists")
public class PList {
    @Id
    private Long id;
    private String name;
}
