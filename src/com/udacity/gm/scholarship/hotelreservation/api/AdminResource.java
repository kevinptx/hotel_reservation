package com.udacity.gm.scholarship.hotelreservation.api;

import com.udacity.gm.scholarship.hotelreservation.model.Customer;
import com.udacity.gm.scholarship.hotelreservation.model.IRoom;
import com.udacity.gm.scholarship.hotelreservation.service.CustomerService;
import com.udacity.gm.scholarship.hotelreservation.service.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {
    private static ReservationService reservationService = ReservationService.getInstance();
    private static CustomerService customerService = CustomerService.getInstance();

    private static AdminResource ADMIN_RESOURCE_INSTANCE = new AdminResource();

    private AdminResource(){}

    public static AdminResource getInstance(){
        return ADMIN_RESOURCE_INSTANCE;
    }

    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }

//    public void addRoom(List<IRoom> rooms){
//        rooms.forEach(reservationService::addRoom);
//    }

    public Collection<IRoom> getAllRooms(){
        return reservationService.getAllRooms();
    }

    public Collection<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    public void displayAllReservations(){
        reservationService.printAllReservations();
    }

    public void addRoom(List<IRoom> rooms) {
        try {
            for (IRoom room : rooms) {
                ReservationService.getInstance().addRoom(room);
            }
        } catch (Exception e) {

        }
        }
    }

