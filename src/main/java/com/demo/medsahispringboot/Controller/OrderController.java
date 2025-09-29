package com.demo.medsahispringboot.Controller;

import com.demo.medsahispringboot.Entity.Medicine;
import com.demo.medsahispringboot.Entity.Order;
import com.demo.medsahispringboot.Entity.User;
import com.demo.medsahispringboot.Repository.MedicineRepository;
import com.demo.medsahispringboot.Repository.OrderRepository;
import com.demo.medsahispringboot.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MedicineRepository medicineRepository;

    public OrderController(OrderRepository orderRepository,
                           UserRepository userRepository,
                           MedicineRepository medicineRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.medicineRepository = medicineRepository;
    }

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(Authentication auth,
                                        @RequestBody List<Long> medicineIds) {
        Optional<User> userOpt = userRepository.findByEmail(auth.getName());
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");
        User user = userOpt.get();

        List<Medicine> medicines = medicineRepository.findAllById(medicineIds);
        double total = medicines.stream().mapToDouble(Medicine::getFinalPrice).sum();

        Order order = new Order();
        order.setUser(user);
        order.setMedicines(medicines);
        order.setTotalAmount(total);
        order.setStatus("PLACED");

        orderRepository.save(order);

        return ResponseEntity.ok(Map.of(
                "message", "Order placed",
                "orderId", order.getId(),
                "totalAmount", total
        ));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<?> myOrders(Authentication auth) {
        Optional<User> userOpt = userRepository.findByEmail(auth.getName());
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");
        User user = userOpt.get();

        List<Order> orders = orderRepository.findByUserId(user.getId());
        return ResponseEntity.ok(orders);
    }
}

