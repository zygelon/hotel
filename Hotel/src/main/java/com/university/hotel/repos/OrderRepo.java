package com.university.hotel.repos;

import com.university.hotel.entities.Order;
import com.university.hotel.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepo extends CrudRepository<Order,Long> {
    List<Order> findByUser(User user);

    @Query("SELECT o FROM Order o WHERE ((?1 > o.dateFrom AND ?1 < o.dateTo) OR (?2 > o.dateFrom AND ?2 < o.dateTo) OR (?1 < o.dateFrom AND ?2 > o.dateTo))")
    List<Order> findOrderByDates(Date dateFrom, Date dateTo);

    List<Order> findAllByDateToGreaterThan(Date date);

    List<Order> findAllByDateToBetween(Date dateFrom, Date dateTo);

    @Query (value = "SELECT MAX(o.dateTo) FROM Order o")
    java.sql.Date getMaxDate();
}
