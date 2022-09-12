package com.udacity.gm.scholarship.hotelreservation.cli;

import com.udacity.gm.scholarship.hotelreservation.api.HotelResource;
import com.udacity.gm.scholarship.hotelreservation.model.Customer;
import com.udacity.gm.scholarship.hotelreservation.model.IRoom;
import com.udacity.gm.scholarship.hotelreservation.model.Reservation;
import com.udacity.gm.scholarship.hotelreservation.service.CustomerService;
import com.udacity.gm.scholarship.hotelreservation.service.ReservationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

import static com.udacity.gm.scholarship.hotelreservation.cli.AdminMenu.displayAdminMenu;
import static com.udacity.gm.scholarship.hotelreservation.service.ReservationService.printRooms;

//Reference: Mentor session 7-27-22, Mentor Session 7-30-22, and Mentor Session 7-31-22
public class MainMenu {
    private static final HotelResource hotelResource = HotelResource.getInstance();

    private static final String ACCEPTABLE_DATE_FORMAT = "MM/dd/yyyy";

    public static void displayMainMenu(Scanner scanner) {
        Scanner mainMenuUserInput = new Scanner(System.in);
        while (true) {
            System.out.println("Welcome to the Hotel Reservation App!");
            System.out.println("=======================================================");
            System.out.println("1. Find and reserve a room.");
            System.out.println("2. See my reservations");
            System.out.println("3. Create an account");
            System.out.println("4. Admin Menu");
            System.out.println("5. Exit");
            System.out.println("=======================================================");
            System.out.println("Please select a number from 1-5 from the above Main Menu.");

            String mainMenuUserInputString = mainMenuUserInput.next();
            while (true) {
                boolean invalidMainMenuOption = false;
                switch (mainMenuUserInputString) {
                    case "1":
                        System.out.println("Selection: Reserve a Room");
                        findAndReserveARoom(mainMenuUserInput);
                        break;
                    case "2":
                        System.out.println("Selection: Display Your Reservations");
                        displayCustomerReservations(mainMenuUserInput);
                        break;
                    case "3":
                        System.out.println("Selection: Create an Account");
                        displayCreateAccount(mainMenuUserInput);
                        break;
                    case "4":
                        System.out.println("Selection: Display Admin Menu");
                        displayAdminMenu(mainMenuUserInput);
                        break;
                    case "5":
                        System.out.println("Thank you! See you next time!");
                        return;
                    default:
                        invalidMainMenuOption = true;
                        System.out.println("Please make a selection from 1 to 5 from the above menu.");
                        mainMenuUserInputString = mainMenuUserInput.next();
                }
                if (!invalidMainMenuOption) {
                    break;
                }
            }
        }
    }

    private static void displayCreateAccount(Scanner scanner)  {
        System.out.println("Enter your email address in the format name@email.com: ");
        String inputEmail = scanner.next();
        System.out.println("First Name: ");
        String firstName = scanner.next();
        System.out.println("Last Name: ");
        String lastName = scanner.next();
        hotelResource.createACustomer(inputEmail, firstName, lastName);
        System.out.println(firstName + " " + lastName + ", Your account was created: \n" + "First Name: " + firstName + "\n" + "Last Name : " + lastName + "\n" + "Email: " +inputEmail + "\n");
    }


    private static void displaySeparatorLine(){
        System.out.println("=======================================================");
        System.out.println();
    }

    public static void showMainMenu(){
        System.out.println("Welcome to the Hotel Reservation App!");
        System.out.println("=======================================================");
        System.out.println("1. Find and reserve a room.");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin Menu");
        System.out.println("5. Exit");
        System.out.println("=======================================================");
        System.out.println("Please select a number from 1-5 from the menu.");
    }

    private static void displayCustomerReservations(Scanner scanner){
        System.out.println("Please provide your customer email:");
        String customerEmail = scanner.nextLine();
        Customer customer = CustomerService.getInstance().getCustomer(customerEmail);
        if(customer == null){
            System.out.println("I am not able to locate the customer with an email address of=" + customerEmail);
            System.out.println("An account must be created first.");
        } else {
            Collection<Reservation> customerReservations = ReservationService.getInstance().getCustomersReservation(customer);
            ReservationService.getInstance().printReservations(customerReservations);
        }
        displaySeparatorLine();
        showMainMenu();
    }

    public static void findAndReserveARoom(Scanner scanner){
        System.out.println("Please enter your check-in date in MM/DD/YYYY format: ");
        Date checkInDate = getUserProvidedDate(scanner);
        System.out.println("Please enter your check-out date in MM/DD/YYYY format: ");
        Date checkOutDate = getUserProvidedDate(scanner);
        System.out.println("Enter your email address in the format name@email.com: ");
        String findCustomerEmail = scanner.next();
        performFindAndReserveARoomChecks(scanner, findCustomerEmail, checkInDate, checkOutDate);
    }

//    public static void performFindAndReserveARoomChecks(Scanner scanner, String inputEmail, Date checkInDate, Date checkOutDate) {
//        Collection<IRoom> roomsAvailable = null;
//        if (checkInDate != null && checkOutDate != null) {
//            roomsAvailable = hotelResource.findARoom(checkInDate, checkOutDate);
//            // BEFORE YOU SHOW THE ROOMSAVAILABLE, HERE IS WHERE YOU NEED TO CHECK IF THE LIST IS EMPTY
//            // if so, then look for the alternate dates
//            if (roomsAvailable.isEmpty()) {
//                Collection<IRoom> suggestedRooms = hotelResource.findSuggestedRooms(checkInDate, checkOutDate);
//                Date suggestedCheckIn = hotelResource.addSuggestedSevenDays(checkInDate);
//                Date suggestedCheckOut = hotelResource.addSuggestedSevenDays(checkOutDate);
//                System.out.println("Your suggested room reservation days are 7 days added to your check-in and check-out dates as follows:" +
//                        "\n Check-In-Date + 7: " + suggestedCheckIn +
//                        "\n Check-Out-Date + 7" + suggestedCheckOut);
//                printRooms(suggestedRooms);
//            }
//        } else {
//            printRooms(roomsAvailable);
//            System.out.println("Select a room from the list: ");
//            String getUserSelectedRoom = scanner.nextLine();
//            IRoom selectedRoom = hotelResource.getRoom(getUserSelectedRoom);
//            Reservation reservation = hotelResource.bookARoom(inputEmail, selectedRoom, checkInDate, checkOutDate);
//            System.out.println("Thank you for booking a room with us! Your Room Details:");
//            System.out.println(reservation.toString());
//        }
//    }


    public static void performFindAndReserveARoomChecks(Scanner scanner, String inputEmail, Date checkInDate, Date checkOutDate) {
        Collection<IRoom> roomsAvailable = hotelResource.findARoom(checkInDate, checkOutDate);
        if (roomsAvailable.isEmpty()) { // if no rooms we will recommend/alternate another dates
            Date suggestedCheckIn = hotelResource.addSuggestedSevenDays(checkInDate);
            Date suggestedCheckOut = hotelResource.addSuggestedSevenDays(checkOutDate);
            roomsAvailable = hotelResource.findARoom(suggestedCheckIn, suggestedCheckOut);
            System.out.println("We can not find rooms based on the provided Check-in and Check-out dates. We recommended rooms for another date");
            printRooms(roomsAvailable);
        } else { // if there are rooms available we just print them
            printRooms(roomsAvailable);
        }
    }

//    private static boolean checkIfAnswerIsYesOrNo(String answerString) {
//        boolean yesOrNoFlag = false;
//        if (answerString != null) {
//            if ("YES".equalsIgnoreCase(answerString) || "Y".equalsIgnoreCase(answerString) ||
//                    "NO".equalsIgnoreCase(answerString) || "N".equalsIgnoreCase(answerString)
//            ) {
//                yesOrNoFlag = true;
//            }
//        }
//        return yesOrNoFlag;
//    }

//    private boolean checkIfAnswerIsYes(String answerString) {
//        boolean isYesFlag = false;
//        if (answerString != null) {
//            if ("YES".equalsIgnoreCase(answerString) || "Y".equalsIgnoreCase(answerString)) {
//                isYesFlag = true;
//            }
//        }
//        return isYesFlag;
//    }


    // source: https://www.baeldung.com/java-iterate-list
//    public void printRooms(Collection<IRoom> rooms){
//        if(rooms == null){
//            System.out.println("No rooms were located.");
//        } else {
//            rooms.stream().forEach((room) -> System.out.println(room));
//        }
//    }

    public static Date getUserProvidedDate(Scanner scanner) {
        try {
            return new SimpleDateFormat(ACCEPTABLE_DATE_FORMAT).parse(scanner.next());
        } catch (ParseException ex) {
            System.out.println("Error: You Entered an Invalid date that is not in MM/DD/YYYY format.");
            findAndReserveARoom(scanner);
        }
        return null;
    }

//    private boolean isYesOrNo(String answerString){
//        boolean yesOrNoFlag = false;
//        if(answerString != null){
//            if("YES".equalsIgnoreCase(answerString) ||
//                    "Y".equalsIgnoreCase(answerString) ||
//                    "N".equalsIgnoreCase(answerString) ||
//                    "NO".equalsIgnoreCase(answerString))
//                yesOrNoFlag = true;
//        }
//        return yesOrNoFlag;
//    }


//    private static boolean answerIsYes(String questionMessage, Scanner scanner){
//        boolean isAnswerYes = false;
//        System.out.println(questionMessage);
//        String answer = scanner.nextLine();
//        while(!isYesOrNo(answer)) {
//            System.out.println("Please enter YES (Y) or NO (N)");
//            System.out.println(questionMessage);
//            answer = scanner.nextLine();
//        }
//        return isAnswerYes(answer);
//    }

//    private static boolean isAnswerYes(String answerString){
//        boolean isYesFlag = false;
//        if(answerString != null){
//            if("YES".equalsIgnoreCase(answerString) ||
//                    "Y".equalsIgnoreCase(answerString))
//                isYesFlag = true;
//        }
//        return isYesFlag;
//    }
}