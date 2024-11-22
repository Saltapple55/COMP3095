package ca.gbc.productservice.dto;

import java.math.BigDecimal;

public record ProductRequest(
    String id, //everything in record is final-immutable
    String name,
    String description,
    BigDecimal price ){
//
//    public ProductRequest(String id, String name, String description, BigDecimal price) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.price = price;
//    }


}
