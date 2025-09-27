package com.demo.medsahispringboot.Controller;


import com.demo.medsahispringboot.Entity.OrderItem;
import com.demo.medsahispringboot.Service.OrderItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping("/{orderId}/{brandedMedicineId}")
    public OrderItem addItem(@PathVariable Long orderId,
                             @PathVariable Long brandedMedicineId,
                             @RequestBody OrderItem orderItem) {
        return orderItemService.addOrderItem(orderItem, orderId, brandedMedicineId);
    }

    @GetMapping("/order/{orderId}")
    public List<OrderItem> getItemsByOrder(@PathVariable Long orderId) {
        return orderItemService.getItemsByOrder(orderId);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
        return "OrderItem deleted successfully with id: " + id;
    }
}
