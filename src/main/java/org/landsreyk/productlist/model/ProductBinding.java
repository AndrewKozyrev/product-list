package org.landsreyk.productlist.model;

import lombok.Data;

@Data
public class ProductBinding {
    private Long id;
    private Long listId;
    private String listName;
}
