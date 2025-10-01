package com.demo.medsahispringboot.Service;

import com.demo.medsahispringboot.Entity.Order;

import java.util.List;

public interface OrderService {
    Order placeOrder(Order order);
    List<Order> getOrdersByUser(String userEmail);
}
