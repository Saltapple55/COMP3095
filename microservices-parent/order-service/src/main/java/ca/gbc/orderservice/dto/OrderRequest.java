package ca.gbc.orderservice.dto;

import java.math.BigDecimal;

public record OrderRequest(
        //normally smaller than model or group collaboratively
        Long id,
        String orderNumber,
        String skuCode,
        BigDecimal price,
        Integer quantity


) {
}
