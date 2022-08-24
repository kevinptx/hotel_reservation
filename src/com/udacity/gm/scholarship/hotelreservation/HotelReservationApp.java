package com.udacity.gm.scholarship.hotelreservation;

import com.udacity.gm.scholarship.hotelreservation.cli.MainMenu;

import java.util.Scanner;

//Reference1
// : Mentor session 7-27-22
public class HotelReservationApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int selection = -1;
        MainMenu.displayMainMenu(scanner);
        if(scanner != null)
            scanner.close();
    }
}
