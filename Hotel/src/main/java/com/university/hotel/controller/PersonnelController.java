package com.university.hotel.controller;

import com.university.hotel.entities.JobType;
import com.university.hotel.entities.Worker;
import com.university.hotel.repos.PersonnelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;

import java.sql.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class PersonnelController {
    private Logger logger = Logger.getLogger(PersonnelController.class.getName());

    private PersonnelRepo personnelRepo;

    public PersonnelController(PersonnelRepo personnelRepo) {
        this.personnelRepo = personnelRepo;
    }

    @GetMapping("/personnel")
    public String personnel(){
        return "personnel";
    }

    @PostMapping("/personnel/add")
    public String addWorker(@RequestParam("firstName") String firstName,
                            @RequestParam("lastName") String lastName,
                            @RequestParam("age") Integer age,
                            @RequestParam("salary") Integer salary,
                            @RequestParam("role") String role,
                            @RequestParam("phone") String phone,
                            @RequestParam("passport") String passport,
                            @RequestParam("workStart") Date workStart,
                            Map<String,Object> model
                            ){
        java.util.Date curDate = new java.util.Date();
        java.util.Date workStartDate = new java.util.Date(workStart.getTime());
        if(personnelRepo.findByPassportNum(passport)!=null){
            model.put("message","Worker already exists");
        }else if(curDate.compareTo(workStartDate) < 0){
            model.put("message","Invalid work start date");
        }else{
            Worker worker = new Worker();
            worker.setFirstName(firstName);
            worker.setLastName(lastName);
            worker.setAge(age);
            worker.setSalary(salary);
            worker.setRole(JobType.valueOf(role));
            worker.setPassportNum(passport);
            worker.setPhone(phone);
            worker.setWorkStart(workStart);
            personnelRepo.save(worker);
            logger.log(Level.INFO,String.format("Worker %s added.",firstName));
        }
        return "personnel";
    }

    @GetMapping("/personnel/all")
    public String getAllPersonnel(/*Map<String,Object>*/ Model model){
        Iterable<Worker>personnel = personnelRepo.findAll();
        //model.put("personnel",personnel);
        model.addAttribute("personnel", personnel);
        return "personnel";
    }

    @GetMapping("/personnel/remove/{id}")
    public String dismissWorker(@PathVariable("id") Worker worker){
        logger.log(Level.INFO,String.format("Worker %d deleted.",worker.getId()));
        personnelRepo.delete(worker);
        return "redirect:/personnel/all";
    }

    @GetMapping("/personnel/salary")
    public String getPersonnelMonthSalary(Map<String,Object> model){
        Integer salary = personnelRepo.getMonthSalary();
        model.put("salary",salary);
        return "personnel";
    }
}
