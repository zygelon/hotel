package com.university.hotel.controller;

import com.university.hotel.entities.User;
import com.university.hotel.repos.UserRepo;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class MainControllerTest {

    @Test(expected = java.lang.NullPointerException.class)
    public void homeTest(){
        User user = new User();
        UserRepo userRepo = Mockito.mock(UserRepo.class);
        Mockito.when(userRepo.findByUsername(Mockito.anyString())).thenReturn(user);
        Mockito.when(userRepo.save(Mockito.any(User.class))).thenReturn(null);
        MainController mainController = new MainController(userRepo);
        Assert.assertEquals("home",mainController.home(null));
    }

    @Test
    public void mainTest(){
        MainController mainController = new MainController(null);
        Assert.assertEquals("main",mainController.main());
    }
}
