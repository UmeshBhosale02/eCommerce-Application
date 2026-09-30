package com.app.ecom_application.dto;

import lombok.Data;

@Data
public class CartItemRequst {
    private Long productId;
    private Integer quantity;
}
