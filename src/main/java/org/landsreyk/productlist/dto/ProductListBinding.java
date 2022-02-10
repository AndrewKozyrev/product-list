package org.landsreyk.productlist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductListBinding {
    private Long id;
    private Long listId;
    private String listName;

    @JsonProperty("list_id")
    public void setListId(Long listId) {
        this.listId = listId;
    }

    @JsonProperty("list_name")
    public void setListName(String listName) {
        this.listName = listName;
    }
}
