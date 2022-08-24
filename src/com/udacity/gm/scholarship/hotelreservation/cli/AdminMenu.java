package com.udacity.gm.scholarship.hotelreservation.cli;

import com.udacity.gm.scholarship.hotelreservation.api.AdminResource;
import com.udacity.gm.scholarship.hotelreservation.model.Customer;
import com.udacity.gm.scholarship.hotelreservation.model.IRoom;
import com.udacity.gm.scholarship.hotelreservation.model.Room;
import com.udacity.gm.scholarship.hotelreservation.model.RoomType;
import com.udacity.gm.scholarship.hotelreservation.service.ReservationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import static com.udacity.gm.scholarship.hotelreservation.cli.MainMenu.showMainMenu;

//Reference: Mentor session 7-27-22, Mentor Session 7-30-22, and Mentor Session 7-31-22
public class AdminMenu {
    private static final AdminResource adminResource = AdminResource.getInstance();
    public static void displayAdminMenu(Scanner scanner) {
        showAdminMenu();
        int selection = -1;
        while (selection != 5) {
            try {
                selection = Integer.parseInt(scanner.nextLine());
                switch (selection) {
                    case 1:
                        displayAllCustomers();
                        displaySeparatorLine();
                        showAdminMenu();
                        break;
                    case 2:
                        ReservationService.getInstance().printAllRooms();
                        displaySeparatorLine();
                        showAdminMenu();
                        break;
                    case 3:
                        ReservationService.getInstance().printAllReservations();
                        displaySeparatorLine();
                        showAdminMenu();
                        break;
                    case 4:
                        System.out.println("Enter a room number: ");
                        String inputRoomNumber = scanner.next();
                        System.out.println("Enter room price: ");
                        double  inputRoomPrice = scanner.nextDouble();
                        System.out.println("Select a room type: Choose SINGLE or DOUBLE");
                       RoomType inputRoomType = RoomType.SINGLE;
                       boolean invalidInput = true;
                        while(true) {
                            String input = scanner.next();
                            switch (input) {
                                case "1": {
                                    inputRoomType = RoomType.SINGLE;
                                    invalidInput = false;
                                    break;
                                }
                                case "2": {
                                    inputRoomType = RoomType.DOUBLE;
                                    invalidInput = false;
                                    break;
                                }
                                default: {
                                    System.out.println("Enter 1 for a SINGLE bed or 2 for a DOUBLE bed.");
                                }
                            }
                            if (!invalidInput) {
                                System.out.println("Input Room Type: " + inputRoomType);
                                Room newRoom = new Room(inputRoomNumber, inputRoomPrice, inputRoomType);
                                List<IRoom> roomsToAdd = new ArrayList<>();
                                AdminResource.getInstance().addRoom(roomsToAdd);
                                displaySeparatorLine();
                                break;
                        }
                    }
                        showAdminMenu();
                    case 5:
                        showMainMenu();
                        break;
                    default:
                        System.out.println("Please make a selection between 1 to 5.");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Error. Please select an integer between 1 to 5.");
            }
        }
    }

    private static void displaySeparatorLine(){
        System.out.println("=======================================================");
        System.out.println();
    }

    static boolean isAnswerNo(String answerString){
        boolean isNoFlag = false;
        if(answerString != null){
            if("NO".equalsIgnoreCase(answerString) ||
                    "N".equalsIgnoreCase(answerString))
                isNoFlag = true;
        }
        return isNoFlag;
    }

    static boolean isYesOrNo(String answerString){
        boolean yesOrNoFlag = false;
        if(answerString != null){
            if("YES".equalsIgnoreCase(answerString) ||
                    "Y".equalsIgnoreCase(answerString) ||
                    "NO".equalsIgnoreCase(answerString) ||
                    "N".equalsIgnoreCase(answerString))
                yesOrNoFlag = true;
        }
        return yesOrNoFlag;
    }

    private static void displayAllCustomers(){
        Collection<Customer> customers = adminResource.getAllCustomers();
        adminResource.getAllCustomers().forEach(System.out::println);
    }
    public static void showAdminMenu(){
        System.out.println("Admin Menu");
        System.out.println("=======================================================");
        System.out.println("1. See all Customers");
        System.out.println("2. See all Rooms");
        System.out.println("3. See all Reservations");
        System.out.println("4. Add a Room");
        System.out.println("5. Return to the Main Menu");
        System.out.println("=======================================================");

    }
}
