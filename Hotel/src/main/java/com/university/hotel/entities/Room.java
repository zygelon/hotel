package com.university.hotel.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name = "room_id")
    private Long roomId;

    private String num;
    private Integer occupancy;
    private Integer price;

    @Enumerated(EnumType.STRING)
    private RoomType type;

    public Room() {
    }

    public Room(String num,Integer occupancy,Integer price,RoomType type) {
        this.num = num;
        this.occupancy = occupancy;
        this.price = price;
        this.type = type;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Integer getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(Integer occupancy) {
        this.occupancy = occupancy;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public RoomType getRoomType() {
        return type;
    }

    public void setRoomType(RoomType type) {
        this.type = type;
    }
}
