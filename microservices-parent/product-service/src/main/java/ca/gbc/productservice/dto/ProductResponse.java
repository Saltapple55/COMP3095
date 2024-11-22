package ca.gbc.productservice.dto;

import java.math.BigDecimal;

public record ProductResponse(
        String id, //everything in record is final-immutable
        String name,
        String description,
        BigDecimal price ){

}
