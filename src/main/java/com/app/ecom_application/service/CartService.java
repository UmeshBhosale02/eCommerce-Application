package com.app.ecom_application.service;

import com.app.ecom_application.dto.CartItemRequst;
import com.app.ecom_application.dto.CartItemResponse;
import com.app.ecom_application.model.CartItem;
import com.app.ecom_application.model.Product;
import com.app.ecom_application.model.User;
import com.app.ecom_application.repository.CartItemRepository;
import com.app.ecom_application.repository.ProductRepository;
import com.app.ecom_application.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    public boolean addToCart(String userId, CartItemRequst requst) {
        Optional<Product> productOpt =productRepository.findById(requst.getProductId());
        if(productOpt.isEmpty())
             return false;

        Product product=productOpt.get();

        if(product.getStockQuantity()<requst.getQuantity())
            return false;

        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));

        if(userOpt.isEmpty())
            return false;

        User user=userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user,product);
        if(existingCartItem !=null){
            existingCartItem.setQuantity(existingCartItem.getQuantity()+ requst.getQuantity());
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
            cartItemRepository.save(existingCartItem);
        }
        else{
            CartItem cartItem=new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(requst.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(requst.getQuantity())));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean deeleteItemFromCart(String userId, Long productId) {
        Optional<Product> productOpt =productRepository.findById(productId);
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));

        if(userOpt.isPresent() && productOpt.isPresent()){
            cartItemRepository.deleteByUserAndProduct(userOpt.get(),productOpt.get());
            return true;
        }
        return false;
    }


    public List<CartItem> GetCartItems(String userId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        return cartItemRepository.findByUser(user);
    }

    public void clearCart(String userID) {
        userRepository.findById(Long.valueOf(userID)).ifPresent(
                cartItemRepository::deleteByUser
        );
    }
}
