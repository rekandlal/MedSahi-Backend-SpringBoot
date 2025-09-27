package com.demo.medsahispringboot.Service.Impl;

import com.demo.medsahispringboot.Entity.Order;
import com.demo.medsahispringboot.Entity.User;
import com.demo.medsahispringboot.Repository.UserRepository;
import com.demo.medsahispringboot.Service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository,UserRepository userRepository){
        this.orderRepository = orderRepository;
        this.userRepository= userRepository;
    }

    @Override
    public Order createOrder(Order order, Long userid){
        User user=userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User Not Found with the given Id:"+userid));
                order.setUser(user);
                order.setOrderDate(LocalDate.now());
                return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id){
        return orderRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Order not Found with the id:"+id));

    }

    @Override
    public List<Order> getOrdersByUser(Long id,Order order){
        Order existing=getOrderById(id);
        existing.setStatus(order.getStatus());
        existing.setPaymentMode(order.getPaymentMode());
        existing.setTotalAmount(order.getTotalAmount());
        return orderRepository.save(existing);
    }

    @Override
    public void deleteOrder(Long id){
        orderRepository.deleteById(id);
    }
}
