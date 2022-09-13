package com.udacity.gm.scholarship.hotelreservation.model;
import java.util.Objects;
import java.util.regex.Pattern;

public class Customer {

    private String firstName;
    private String lastName;
    private String email;

    private Pattern emailPattern;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(firstName, customer.firstName) && Objects.equals(lastName, customer.lastName) && Objects.equals(email, customer.email) && Objects.equals(emailPattern, customer.emailPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, emailPattern);
    }

    //Reference: https://classroom.udacity.com/nanodegrees/nd079-gm/parts/cd0282/modules/6bcd777d-42b3-444d-aec5-59b10ce798e3/lessons/952eff79-8e41-4076-8705-5a74417095d3/concepts/bd1aeff5-27ae-4337-8146-37fe418de139
    private static final String EMAIL_REGEX = "^(.+)@(.+)\\.(.+)$";

    public Customer(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isEmailValid(email);
    }

    private void isEmailValid(String email){
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        if(!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("The input is not valid. The email entered was:" + email);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
