package com.demo.medsahispringboot.Controller;

import com.demo.medsahispringboot.Entity.GenericMedicine;
import com.demo.medsahispringboot.Entity.Order;
import com.demo.medsahispringboot.Entity.User;
import com.demo.medsahispringboot.Repository.GenericMedicineRepository;
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
        private final GenericMedicineRepository genericMedicineRepository;

        public OrderController(OrderRepository orderRepository,
                               UserRepository userRepository,
                               GenericMedicineRepository genericMedicineRepository) {
            this.orderRepository = orderRepository;
            this.userRepository = userRepository;
            this.genericMedicineRepository = genericMedicineRepository;
        }

        // Place order (generic only)
        @PostMapping("/place")
        public ResponseEntity<?> placeOrder(Authentication auth,
                                            @RequestBody List<Long> genericMedicineIds) {
            Optional<User> userOpt = userRepository.findByEmail(auth.getName());
            if (userOpt.isEmpty())
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            User user = userOpt.get();

            List<GenericMedicine> medicines = genericMedicineRepository.findAllById(genericMedicineIds);
            if (medicines.isEmpty())
                return ResponseEntity.badRequest().body(Map.of("error", "No generic medicines found"));

            double total = medicines.stream().mapToDouble(GenericMedicine::getFinalPrice).sum();

            Order order = new Order();
            order.setUser(user);
            order.setGenericMedicines(medicines); // make sure Order entity has genericMedicines list
            order.setTotalAmount(total);
            order.setStatus("PLACED");

            orderRepository.save(order);

            return ResponseEntity.ok(Map.of(
                    "message", "Order placed",
                    "orderId", order.getId(),
                    "totalAmount", total
            ));
        }

        // Get orders of logged-in user
        @GetMapping("/my-orders")
        public ResponseEntity<?> myOrders(Authentication auth) {
            Optional<User> userOpt = userRepository.findByEmail(auth.getName());
            if (userOpt.isEmpty())
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            User user = userOpt.get();

            List<Order> orders = orderRepository.findByUserId(user.getId());
            return ResponseEntity.ok(orders);
        }



//    // Admin / Pharmacist: get all orders
//    @GetMapping("/all")
//    public ResponseEntity<?> allOrders() {
//        List<Order> orders = orderRepository.findAll();
//        return ResponseEntity.ok(orders);
//    }
}
