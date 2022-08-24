package com.udacity.gm.scholarship.hotelreservation.model;

public interface IRoom {
    String getRoomNumber();

    Double getRoomPrice();

    RoomType getRoomType();

    Boolean isFree();
}
