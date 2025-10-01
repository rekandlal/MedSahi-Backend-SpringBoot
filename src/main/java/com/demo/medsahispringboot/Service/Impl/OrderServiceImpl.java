package com.demo.medsahispringboot.Service.Impl;

import com.demo.medsahispringboot.Entity.Order;
import com.demo.medsahispringboot.Repository.OrderRepository;
import com.demo.medsahispringboot.Service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order placeOrder(Order order) {
        order.setOrderTime(LocalDateTime.now());
        order.setStatus("PLACED");
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByUser(String userEmail) {
        return orderRepository.findByUserEmail(userEmail);
    }
}
