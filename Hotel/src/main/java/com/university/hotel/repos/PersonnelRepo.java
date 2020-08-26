package com.university.hotel.repos;

import com.university.hotel.entities.Worker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PersonnelRepo extends CrudRepository<Worker,Integer> {
    Worker findByPassportNum(String passportNum);

    @Query("SELECT SUM(w.salary) FROM Worker w")
    Integer getMonthSalary();
}
