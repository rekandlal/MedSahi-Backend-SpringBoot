package com.demo.medsahispringboot.Service;

import com.demo.medsahispringboot.Entity.OrderItem;

import java.util.List;

public interface OrderItemService {
    OrderItem addOrderItem(OrderItem orderItem,Long orderId,Long brandedMedicineId);
    List<OrderItem> getItemsByOrder(Long orderId);
    void deleteOrderItem(Long id);
}
