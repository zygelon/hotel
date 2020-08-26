package com.university.hotel.controller;

import com.university.hotel.entities.Worker;
import com.university.hotel.repos.PersonnelRepo;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PersonnelControllerTest {
    @Test
    public void addWorkerTest(){
        PersonnelRepo personnelRepo = Mockito.mock(PersonnelRepo.class);
        Mockito.when(personnelRepo.save(Mockito.any())).thenReturn(null);
        Mockito.when(personnelRepo.findByPassportNum(Mockito.anyString())).thenReturn(null);
        PersonnelController personnelController = new PersonnelController(personnelRepo);
        Map<String,Object> model = new HashMap<>();
        Date date = new Date();
        java.sql.Date date1 = new java.sql.Date(date.getTime());

        String res = personnelController.addWorker("stas","d",20,100,"CLEANER","123","123",date1,model);
        Assert.assertEquals(0,model.size());
        Assert.assertEquals("personnel",res);

        Mockito.when(personnelRepo.findByPassportNum(Mockito.anyString())).thenReturn(new Worker());
        personnelController.addWorker("stas","d",20,100,"CLEANER","123","123",date1,model);
        Assert.assertTrue(model.size() > 0);

    }
}
