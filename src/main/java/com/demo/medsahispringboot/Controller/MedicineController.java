package com.demo.medsahispringboot.Controller;

import com.demo.medsahispringboot.Dto.MedicineDto;
import com.demo.medsahispringboot.Entity.GenericMedicine;
import com.demo.medsahispringboot.Entity.Medicine;
import com.demo.medsahispringboot.Entity.Order;
import com.demo.medsahispringboot.Service.MedicineService;
import com.demo.medsahispringboot.Service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medicine")
public class MedicineController {

    private final MedicineService medicineService;
    private final OrderService orderService;

    public MedicineController(MedicineService medicineService, OrderService orderService) {
        this.medicineService = medicineService;
        this.orderService = orderService;
    }

    // USER: Branded search
    @GetMapping("/search/branded")
    public ResponseEntity<List<MedicineDto>> searchBranded(@RequestParam String keyword) {
        return ResponseEntity.ok(medicineService.searchMedicine(keyword));
    }

    // USER: Generic search only
    @GetMapping("/search/generic")
    public ResponseEntity<List<GenericMedicine>> searchGeneric(@RequestParam String keyword) {
        return ResponseEntity.ok(medicineService.searchGeneric(keyword));
    }

    // USER: Place an order
    @PostMapping("/order")
    public ResponseEntity<?> placeOrder(@RequestBody Order order) {
        Order saved = orderService.placeOrder(order);
        return ResponseEntity.ok(saved);
    }

    // USER: Orders history
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrders(@RequestParam String userEmail) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userEmail));
    }

    // ADMIN / PHARMACIST: Add new medicine
    @PostMapping("/add")
    public ResponseEntity<?> addMedicine(@RequestBody Medicine medicine) {
        Medicine saved = medicineService.addMedicine(medicine);
        return ResponseEntity.ok(saved);
    }

    // ADMIN / PHARMACIST: Delete medicine
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMedicine(@PathVariable Long id) {
        boolean deleted = medicineService.deleteById(id);
        if (!deleted) return ResponseEntity.status(404).body(Map.of("error","Medicine not found"));
        return ResponseEntity.ok(Map.of("message","Deleted successfully"));
    }
}
