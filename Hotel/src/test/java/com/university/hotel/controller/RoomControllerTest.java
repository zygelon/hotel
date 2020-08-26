package com.university.hotel.controller;

import com.university.hotel.entities.Order;
import com.university.hotel.entities.Room;
import com.university.hotel.repos.OrderRepo;
import com.university.hotel.repos.RoomRepo;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomControllerTest {

    @Test
    public void suitableRoomsTest() throws ParseException {

        RoomRepo roomRepo = Mockito.mock(RoomRepo.class);
        OrderRepo orderRepo = Mockito.mock(OrderRepo.class);
        RoomController roomController = new RoomController(roomRepo,orderRepo);
        String [] categories = {"ECONOMIC","STANDARD","LUXURY"};
        String date1="2030-03-10";
        Date dateFrom= Date.valueOf(date1);
        String date2="2030-04-10";
        Date dateTo= Date.valueOf(date2);
        Map<String,Object> model = new HashMap<>();

        Mockito.when(roomRepo.findByPriceBetweenAndOccupancyAndTypeIn(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyInt(),
                Mockito.any())).thenReturn(getAllTestRooms());
        Mockito.when(orderRepo.findOrderByDates(Mockito.any(),Mockito.any())).thenReturn(getOrders());
        String res = roomController.suitableRooms(1,500,1,categories,dateFrom,dateTo,model);
        Assert.assertEquals("suitable_rooms",res);
        List<Room>suitableRooms = (List)model.get("rooms");
        Assert.assertEquals(Long.valueOf(2),suitableRooms.get(0).getRoomId());
        Assert.assertEquals(null,suitableRooms.get(1).getRoomId());
        Assert.assertEquals(dateFrom,model.get("dateFrom"));
        Assert.assertEquals(dateTo,model.get("dateTo"));
    }

    public List<Room> getAllTestRooms(){
        List<Room> rooms= new ArrayList<>();
        Room room1 = new Room();
        room1.setRoomId(Long.parseLong("1"));
        Room room2 = new Room();
        room1.setRoomId(Long.parseLong("2"));
        rooms.add(room1);
        rooms.add(room2);
        return rooms;
    }

    public List<Order>getOrders(){
        List<Order> orders = new ArrayList<>();
        Room room1 = new Room();
        room1.setRoomId(Long.parseLong("1"));
        Order order = new Order();
        order.setRoom(room1);
        orders.add(order);
        return orders;
    }
}
