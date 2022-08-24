package com.udacity.gm.scholarship.hotelreservation.model;

public abstract class FreeRoom extends Room {

    public FreeRoom(String roomNumber, RoomType enumeration){
        super(roomNumber, 0.0, enumeration);
    }

}
