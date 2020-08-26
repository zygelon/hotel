package com.university.hotel.controller;

import com.university.hotel.entities.Order;
import com.university.hotel.entities.Room;
import com.university.hotel.entities.RoomType;
import com.university.hotel.repos.OrderRepo;
import com.university.hotel.repos.RoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class RoomController {

    private Logger logger = Logger.getLogger(RoomController.class.getName());

    private RoomRepo roomRepo;
    private OrderRepo orderRepo;

    public RoomController(RoomRepo roomRepo, OrderRepo orderRepo) {
        this.roomRepo = roomRepo;
        this.orderRepo = orderRepo;
    }

    @PostMapping("/suitableRooms")
    public String suitableRooms(@RequestParam("priceFrom") Integer priceFrom,
                                @RequestParam("priceTo") Integer priceTo,
                                @RequestParam("occupancy") Integer occupancy,
                                @RequestParam("categories") String[] categories,
                                @RequestParam("dateFrom") Date dateFrom,
                                @RequestParam("dateTo") Date dateTo,
                                Map<String,Object> model){
        java.util.Date curDate = new java.util.Date();
        java.util.Date from = new java.util.Date(dateFrom.getTime());

        if(curDate.compareTo(from) > 0){
            model.put("message","Invalid date. Please, choose correct date!");
            return "order";
        }

        RoomType[]roomTypes = new RoomType[categories.length];
        for(int i = 0; i < categories.length; i++){
            roomTypes[i] = (RoomType.valueOf(categories[i]));
        }
        List<Room>rooms = roomRepo.findByPriceBetweenAndOccupancyAndTypeIn(priceFrom, priceTo, occupancy,roomTypes);
        List<Order>orders = orderRepo.findOrderByDates(dateFrom,dateTo);
        for(Order order:orders){
            rooms.removeIf(curRoom -> (curRoom.getRoomId() == order.getRoom().getRoomId()));
        }
        if(rooms == null || rooms.isEmpty()){
            model.put("message","No available rooms with these properties");
            return "order";
        }
        model.put("rooms",rooms);
        model.put("dateFrom",dateFrom);
        model.put("dateTo",dateTo);
        return "suitable_rooms";
    }

    @GetMapping("/rooms")
    public String rooms(){
        return "rooms";
    }

    @PostMapping("/rooms/add")
    public String addRoom(@RequestParam("price") Integer price,
                          @RequestParam("occupancy") Integer occupancy,
                          @RequestParam("number") String number,
                          @RequestParam("type") String type,
                          Map<String,Object> model){
        if(roomRepo.findByNum(number) != null){
            model.put("message", "Room already exists");
        }else{
            Room newRoom = new Room(number,occupancy,price, RoomType.valueOf(type));
            roomRepo.save(newRoom);
            logger.log(Level.INFO,String.format("Room %s added.",newRoom.getNum()));
            model.put("message", "Room added");
        }
        return "redirect:/rooms/all";
    }

    @GetMapping("/rooms/all")
    public String getAllRooms(Map<String,Object> model){
        Iterable<Room>rooms = roomRepo.findAll();
        model.put("rooms",rooms);
        return "rooms";
    }

    @GetMapping("/rooms/remove/{roomId}")
    public String removeRoom(@PathVariable("roomId") Room room){
        logger.log(Level.INFO,String.format("Room %s deleted.",room.getNum()));
        roomRepo.delete(room);
        return "redirect:/rooms/all";
    }
}
