package com.udacity.gm.scholarship.hotelreservation.service;
import com.udacity.gm.scholarship.hotelreservation.model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//Reference: https://classroom.udacity.com/nanodegrees/nd079-gm/parts/cd0282/modules/6bcd777d-42b3-444d-aec5-59b10ce798e3/lessons/2a238c3e-c141-4982-8b85-97332dd069b2/concepts/325da655-78a4-4f44-95bd-21994c8f08c0
public class CustomerService {
    //Reference: https://www.geeksforgeeks.org/singleton-class-java/
    private CustomerService(){}
    private static CustomerService CUSTOMER_SERVICE_INSTANCE = null;

    //adapted from: https://www.geeksforgeeks.org/singleton-class-java/
    public static CustomerService getInstance(){
        if(CUSTOMER_SERVICE_INSTANCE == null){
            CUSTOMER_SERVICE_INSTANCE = new CustomerService();
        }
        return CUSTOMER_SERVICE_INSTANCE;
    }
    private Map<String, Customer> mapOfCustomers = new HashMap<>();

    public void addCustomer(String email, String firstName, String lastName) throws
            IllegalArgumentException {
        Customer newCustomer = new Customer(firstName, lastName, email);
        if(!isCustomerAlreadyExists(email)){
            mapOfCustomers.put(email, newCustomer);
        } else {
            throw new IllegalArgumentException("The input email" + email + "already exists in our database!");
        }
    }

    private boolean isCustomerAlreadyExists(String email) {
        boolean customerExistsFlag = false;
        Customer customer = mapOfCustomers.get(email);
        if(customer != null)
            customerExistsFlag = true;
        return customerExistsFlag;
    }

    public Customer getCustomer(String customerEmail){
        return mapOfCustomers.get(customerEmail);
    }


    public Collection<Customer> getAllCustomers(){
        return mapOfCustomers.values();
    }

}