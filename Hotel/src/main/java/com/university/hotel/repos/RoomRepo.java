package com.university.hotel.repos;

import com.university.hotel.entities.Room;
import com.university.hotel.entities.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface RoomRepo extends JpaRepository<Room,Long> {
    List<Room> findByPriceBetweenAndOccupancyAndTypeIn(Integer priceFrom, Integer priceTo, Integer occupancy, RoomType[]types);
    Room findByNum(String number);

    @Query(value = "SELECT * FROM ROOMS INNER JOIN ORDERS ON ROOMS.id = ORDERS.room_id WHERE (((?1 > ORDERS.dateFrom AND ?1 < ORDERS.dateTo) " +
            "OR (?2 > ORDERS.dateFrom AND ?2 < ORDERS.dateTo) OR (?1 < ORDERS.dateFrom AND ?2 > ORDERS.dateTo)))",
    nativeQuery = true)
    List<Room> findReservedRoomsInDates(Date dateFrom,Date dateTo);
}
