package com.university.hotel.controller;

import com.university.hotel.entities.User;
import com.university.hotel.repos.UserRepo;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserControllerTest {

    @Test
    public void allUsersTest(){
        UserRepo userRepo = Mockito.mock(UserRepo.class);
        Map<String,Object> model = new HashMap<>();
        UserController userController = new UserController(userRepo,null);
        List<User>users = new ArrayList<>();
        User user = new User();
        users.add(user);
        Mockito.when(userRepo.findAll()).thenReturn(users);
        String res = userController.allUsers(model);
        Assert.assertEquals(users,model.get("users"));
        Assert.assertEquals("users",res);
    }
}
