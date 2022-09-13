package com.udacity.gm.scholarship.hotelreservation.cli;

import com.udacity.gm.scholarship.hotelreservation.api.AdminResource;
import com.udacity.gm.scholarship.hotelreservation.model.*;
import com.udacity.gm.scholarship.hotelreservation.service.ReservationService;

import java.util.Collection;
import java.util.Scanner;

//Reference: Mentor session 7-27-22, Mentor Session 7-30-22, and Mentor Session 7-31-22
public class AdminMenu {
    private static final AdminResource adminResource = AdminResource.getInstance();

    public static void displayAdminMenu(Scanner input) {

        while (true) {
            System.out.println("Admin Menu");
            System.out.println("=======================================================");
            System.out.println("1. See all Customers");
            System.out.println("2. See all Rooms");
            System.out.println("3. See all Reservations");
            System.out.println("4. Add a Room");
            System.out.println("5. Return to the Main Menu");
            System.out.println("=======================================================");

            String adminUserInput = input.nextLine();
            while (true) {
                boolean invalidInput = false;
                switch (adminUserInput) {
                    case "1":
                        displayAllCustomers();
                        displaySeparatorLine();
                        break;
                    case "2":
                        displayAllRooms();
                        displaySeparatorLine();
                        break;
                    case "3":
                        displayAllReservations();
                        displaySeparatorLine();
                        break;
                    case "4":
                        addARoom(input);
                        displaySeparatorLine();
                        break;
                    case "5":
                        return;
                    default:
                        invalidInput = true;
                        System.out.println("Please make a selection between 1 to 5.");
                        adminUserInput = input.nextLine();
                }
                if (!invalidInput) {
                    break;
                }
            }
        }
    }

    private static void displayAllRooms() {
        ReservationService.getInstance().printAllRooms();
    }

    private static void displayAllReservations() {
        ReservationService.getInstance().printAllReservations();
    }

    private static void addARoom(Scanner scanner) {
        IRoom createNewRoom;
        String roomNumber;
        Double roomPrice;
        RoomType roomType;
        Collection<IRoom> allRooms = AdminResource.getInstance().getAllRooms();
//        while (true) {
        System.out.println("Enter room number: ");
        roomNumber = scanner.nextLine();
        while (true) {
            System.out.println("Enter Room Price: ");
            try {
                roomPrice = Double.valueOf(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("You failed to enter a number. Try again.");
            }
        }
        while (true) {
            System.out.println("SINGLE or DOUBLE room? Write SINGLE or DOUBLE: ");  // 1.single,2.double
            String userSingleOrDoubleInput = scanner.nextLine();
            if (userSingleOrDoubleInput.equalsIgnoreCase("SINGLE")) {
                roomType = RoomType.SINGLE;
                break;
            } else if (userSingleOrDoubleInput.equalsIgnoreCase("DOUBLE")) {
                roomType = RoomType.DOUBLE;
                break;
            }
        }

        if (roomPrice == 0.0) {
            createNewRoom = new FreeRoom(roomNumber, roomType);
        } else {
            createNewRoom = new Room(roomNumber, roomPrice, roomType);
        }
        ReservationService.getInstance().addRoom(createNewRoom);
        System.out.println("Room has been ADDED...");
    }


    private static void displaySeparatorLine() {
        System.out.println("=======================================================");
        System.out.println();
    }

    private static boolean isYesOrNo(Scanner scanner) {
        while (true) {
            String userAnswer = scanner.nextLine();
            if (userAnswer.equalsIgnoreCase("Y")) {
                return true;
            } else if (userAnswer.equalsIgnoreCase("N")) {
                return false;
            } else {
                System.out.println("Please enter Yes or No. Choose Y for Yes or N for No");
            }

        }
    }

    private static void displayAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();
        adminResource.getAllCustomers().forEach(System.out::println);
    }
}