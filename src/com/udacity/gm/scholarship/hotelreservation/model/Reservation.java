package com.udacity.gm.scholarship.hotelreservation.model;
import java.util.Date;
import java.util.Objects;


public class Reservation {
    private final Customer customer;
    private final IRoom room;
    private final Date checkInDate;
    private final Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public IRoom getRoom(){
        return this.room;
    }

    public Date getCheckInDate(){
        return this.checkInDate;
    }

    public Date getCheckOutDate(){
        return this.checkOutDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }



    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "customer=" + customer +
                ", room=" + room +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                '}';
    }
}