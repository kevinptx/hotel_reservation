package com.udacity.gm.scholarship.hotelreservation.model;

public class FreeRoom extends Room {

    public FreeRoom(String roomNumber, RoomType enumeration){
        super(roomNumber, 0.0, enumeration);
    }

    @Override
    public String toString() {
        return "Room Number: " + roomNumber + "\nPrice: Free \nRoom Type: " + roomType;
    }
}
