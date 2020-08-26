package com.university.hotel.controller;

import com.university.hotel.entities.Order;
import com.university.hotel.entities.Role;
import com.university.hotel.entities.User;
import com.university.hotel.entities.utils.UserConverter;
import com.university.hotel.repos.OrderRepo;
import com.university.hotel.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    private UserRepo userRepo;
    private OrderRepo orderRepo;

    public UserController(UserRepo userRepo, OrderRepo orderRepo) {
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
    }

    @GetMapping("/users")
    public String allUsers(Map<String,Object> model){
        Iterable<User>users = userRepo.findAll();
        model.put("users",users);
        return "users";
    }

    @GetMapping("/users/{userId}")
    public String getUserById(@PathVariable("userId") User user,Map<String,Object> model){
        List<User>users = new ArrayList<>();
        users.add(user);
        model.put("users",users);
        return "users";
    }

    @GetMapping("/users/orders/{userId}")
    public String userOrders(@PathVariable("userId") User user,Map<String,Object> model){
        Iterable<Order> userOrders = orderRepo.findByUser(user);
        model.put("orders",userOrders);
        return "user_orders";
    }

    @GetMapping("/users/orders")
    public String myOrders(Principal p, Map<String,Object> model){
        User dbUser = userRepo.findByUsername(UserConverter.getUserFromKeycloakPrincipal(p).getUsername());
        Iterable<Order> userOrders = orderRepo.findByUser(dbUser);
        model.put("orders",userOrders);
        return "user_orders";
    }
}
