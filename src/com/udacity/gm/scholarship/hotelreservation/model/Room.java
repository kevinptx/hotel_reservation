package com.udacity.gm.scholarship.hotelreservation.model;

import java.util.Objects;

public class Room implements IRoom {
    public String roomNumber;

    public Double price;
    public RoomType roomType;

    public Room (String roomNumber, Double price, RoomType roomType){
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomType = roomType;
    }

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return price;
    }

    public void setRoomPrice(Double price) {
        this.price = price;
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    @Override
    public Boolean isFree(){
        return this.price == 0.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return Objects.equals(roomNumber, room.roomNumber) && Objects.equals(price, room.price) && roomType == room.roomType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber, price, roomType);
    }

    @Override
    public String toString() {
        return "{" +
                "roomNumber='" + roomNumber + '\'' +
                ", price=" + price +
                ", roomType=" + roomType +
                '}';
    }
}


