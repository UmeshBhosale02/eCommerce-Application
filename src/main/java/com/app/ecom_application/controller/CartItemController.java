package com.app.ecom_application.controller;

import com.app.ecom_application.dto.CartItemRequst;
import com.app.ecom_application.dto.CartItemResponse;
import com.app.ecom_application.model.CartItem;
import com.app.ecom_application.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartItemController {

    private final CartService cartService;
    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody CartItemRequst requst
    )
    {
        if(cartService.addToCart(userId,requst))
            return ResponseEntity.status(HttpStatus.CREATED).build();

        return ResponseEntity.badRequest().body("Product out of stock");
    }

    @DeleteMapping("/item/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable Long productId
    ){
       boolean deleted= cartService.deeleteItemFromCart(userId,productId);
       return deleted? ResponseEntity.noContent().build()
               :ResponseEntity.notFound().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> GetCart( @PathVariable String userId){
        List<CartItem> items = cartService.GetCartItems(userId);

        if (items.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(items);
    }


}
