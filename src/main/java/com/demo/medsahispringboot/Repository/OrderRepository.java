package com.demo.medsahispringboot.Repository;

import com.demo.medsahispringboot.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find orders by user ID
    List<Order> findByUserId(Long userId);

    // Find orders by user email (for OrderServiceImpl)
    List<Order> findByUserEmail(String email);
}
