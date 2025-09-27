package com.demo.medsahispringboot.Service.Impl;

import com.demo.medsahispringboot.Entity.BrandedMedicine;
import com.demo.medsahispringboot.Entity.Order;
import com.demo.medsahispringboot.Entity.OrderItem;
import com.demo.medsahispringboot.Repository.BrandedMedicineRepository;
import com.demo.medsahispringboot.Repository.OrderItemRepository;
import com.demo.medsahispringboot.Repository.OrderRepository;
import com.demo.medsahispringboot.Service.OrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final BrandedMedicineRepository brandedMedicineRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository,
                                OrderRepository orderRepository,
                                BrandedMedicineRepository brandedMedicineRepository){
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.brandedMedicineRepository = brandedMedicineRepository;
    }

    @Override
    public OrderItem addOrderItem(OrderItem orderItem,Long orderId,Long brandedMedicineId){
        Order order=orderRepository.findById(orderId)
                .orElseThrow(()->new RuntimeException("Order not found with the id:"+orderId));

        BrandedMedicine brandedMedicine=brandedMedicineRepository.findById(brandedMedicineId)
                .orElseThrow(()->new RuntimeException("BrandedMedicine not found with id:"+brandedMedicineId));

        orderItem.setOrder(order);
        orderItem.setBrandedMedicine(brandedMedicine);
        orderItem.setPrice(brandedMedicine.getPrice()*orderItem.getQuantity());
        return orderItemRepository.save(orderItem);

    }

    @Override
    public List<OrderItem> getItemsByOrder(Long orderId){
        return orderItemRepository.findByOrderId(orderId);
    }

    @Override
    public void deleteOrderItem(Long id){
        orderItemRepository.deleteById(id);
    }
}
