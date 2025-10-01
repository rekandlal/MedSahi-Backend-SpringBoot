package com.demo.medsahispringboot.Service;

import com.demo.medsahispringboot.Entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerUser(User user);  // Registration

    Optional<User> findByEmail(String email);

    List<User> getAllUsers();

    User updateUser(User user);

    boolean addRewardCoins(String email, Long coins);

    // Future: subscription handling
    // boolean subscribeUser(String email, Long subscriptionId);
}
