package com.app.ecom_application.service;

import com.app.ecom_application.dto.OrderItemDTO;
import com.app.ecom_application.dto.OrderResponse;
import com.app.ecom_application.model.*;
import com.app.ecom_application.repository.OrderRepository;
import com.app.ecom_application.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final UserRepository userRepository;
    private  final OrderRepository orderRepository;
    public Optional<OrderResponse> createOrder(String userID) {
        //validate itme in cart
        List<CartItem> cartitem =cartService.GetCartItems(userID);
        if(cartitem.isEmpty()){
            return Optional.empty();
        }
        //validate for user
        Optional<User> userOptional=userRepository.findById(Long.valueOf(userID));
        if(userOptional.isEmpty()){
            return Optional.empty();
        }
        User user=userOptional.get();
        //calculate total price
        BigDecimal totalPrice=cartitem.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        //create order
        Orders order= new Orders();

        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems =cartitem.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                )).toList();

        order.setItems(orderItems);

        Orders savedOrder = orderRepository.save(order);
        //clear the cart
        cartService.clearCart(userID);

        return Optional.of(mapTOOrderResponse(savedOrder));

    }

    private OrderResponse mapTOOrderResponse(Orders savedOrder) {
        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus(),
                savedOrder.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProduct().getId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice()
                        )).toList(),
                savedOrder.getCreatedAt()
        );
    }
}
