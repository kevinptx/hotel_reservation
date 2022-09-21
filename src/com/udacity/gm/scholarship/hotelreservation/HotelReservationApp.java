package com.udacity.gm.scholarship.hotelreservation;

import com.udacity.gm.scholarship.hotelreservation.cli.MainMenu;

import java.text.ParseException;
import java.util.Scanner;

//Reference1
// : Mentor session 7-27-22
public class HotelReservationApp {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int selection = -1;
        MainMenu.displayMainMenu(scanner);
        scanner.close();
    }
}
