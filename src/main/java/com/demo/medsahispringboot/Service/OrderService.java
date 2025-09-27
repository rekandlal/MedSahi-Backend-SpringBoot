package com.demo.medsahispringboot.Service;

import com.demo.medsahispringboot.Entity.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(Order order,Long userId);
    List<Order> getAllOrders();
    Order getOrderById(Long id);
    List<Order> getOrdersByUser(Long userId);
    Order updateOrder(Long id,Order order);
    void deleteOrder(Long id);
}
