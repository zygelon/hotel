package com.university.hotel.controller;

import com.university.hotel.entities.User;
import com.university.hotel.entities.utils.UserConverter;
import com.university.hotel.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class MainController {

    private UserRepo userRepo;

    public MainController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/")
    public String main(){
        return "main";
    }

    @GetMapping("/home")
    public String home(Principal p){
        User u = UserConverter.getUserFromKeycloakPrincipal(p);
        if(userRepo.findByUsername(u.getUsername()) == null){
            userRepo.save(u);
        }
        return "home";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }


    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "/";
    }
}
