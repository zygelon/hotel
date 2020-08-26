package com.university.hotel.controller;

import com.university.hotel.entities.Order;
import com.university.hotel.entities.Room;
import com.university.hotel.entities.User;
import com.university.hotel.entities.utils.UserConverter;
import com.university.hotel.repos.OrderRepo;
import com.university.hotel.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.ui.Model;

@Controller
public class OrderController {
    private Logger logger = Logger.getLogger(OrderController.class.getName());

    private OrderRepo orderRepo;
    private UserRepo userRepo;

    public OrderController(OrderRepo orderRepo, UserRepo userRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/order")
    public String order(){
        return "order";
    }

    @GetMapping("/order/{roomId}")
    public String reserveRoom(Principal p,
                              @PathVariable("roomId") Room room,
                              @RequestParam("dateFrom") Date dateFrom,
                              @RequestParam("dateTo") Date dateTo
                              ){
        User user = UserConverter.getUserFromKeycloakPrincipal(p);
        User dbUser = userRepo.findByUsername(user.getUsername());
        Order order = new Order(dateFrom,dateTo,dbUser,room);
        orderRepo.save(order);
        logger.log(Level.INFO,String.format("User %s reserved room %s",user.getUsername(),room.getNum()));
        return "home";
    }

    @GetMapping("/orders/all")
    public String allOrders(Model model){
        Iterable<Order> orders = orderRepo.findAll();
        model.addAttribute("orders", orders);
        model.addAttribute("ordersNum",orderRepo.count());
        return "orders";
    }

    @GetMapping("/orders/active")
    public String activeOrders(Model model){
        List<Order> orders = orderRepo.findAllByDateToGreaterThan(new java.util.Date());
        model.addAttribute("orders", orders);
        model.addAttribute("ordersNum",orders.size());
        int price = 0;
        for(Order o : orders){
            price+=o.getRoom().getPrice();
        }
        model.addAttribute("totalPrice",price);
        return "orders";
    }
}
