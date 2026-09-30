package com.app.ecom_application.dto;

import com.app.ecom_application.model.Product;
import com.app.ecom_application.model.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CartItemResponse {

    private Long id;
    @ManyToOne
    @JoinColumn(name="product_id",nullable = false)
    private Product product;
    private Integer quantity;
    private BigDecimal price;
}
