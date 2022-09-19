package com.udacity.gm.scholarship.hotelreservation.cli;

import com.udacity.gm.scholarship.hotelreservation.api.AdminResource;
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
import java.util.Locale;
import java.util.Scanner;

import static com.udacity.gm.scholarship.hotelreservation.cli.AdminMenu.displayAdminMenu;
//import static com.udacity.gm.scholarship.hotelreservation.service.ReservationService.printRooms;

//Reference: Mentor session 7-27-22, Mentor Session 7-30-22, and Mentor Session 7-31-22
public class MainMenu {
    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static AdminResource adminResource = AdminResource.getInstance();

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

            String mainMenuUserInputString = mainMenuUserInput.nextLine();
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
                        mainMenuUserInputString = mainMenuUserInput.nextLine();
                }
                if (!invalidMainMenuOption) {
                    break;
                }
            }
        }
    }

    private static void displayCreateAccount(Scanner scanner) {
        System.out.println("Enter your email address in the format name@email.com: ");
        String inputEmail = scanner.nextLine();
        System.out.println("First Name: ");
        String firstName = scanner.nextLine();
        System.out.println("Last Name: ");
        String lastName = scanner.nextLine();
        hotelResource.createACustomer(inputEmail, firstName, lastName);
        System.out.println(firstName + " " + lastName + ", Your account was created: \n" + "First Name: " + firstName + "\n" + "Last Name : " + lastName + "\n" + "Email: " + inputEmail + "\n");
    }


    private static void displaySeparatorLine() {
        System.out.println("=======================================================");
        System.out.println();
    }


    private static void displayCustomerReservations(Scanner scanner) {
        System.out.println("Please provide your customer email:");
        String customerEmail = scanner.nextLine();
        Customer customer = CustomerService.getInstance().getCustomer(customerEmail);
        if (customer == null) {
            System.out.println("I am not able to locate the customer with an email address of=" + customerEmail);
            System.out.println("An account must be created first.");
        } else {
            Collection<Reservation> customerReservations = ReservationService.getInstance().getCustomersReservation(customer);
            ReservationService.getInstance().printReservations(customerReservations);
        }
        displaySeparatorLine();
        displayMainMenu(scanner);
    }

    //diff approach based on michael's example:

    public static void findAndReserveARoom(Scanner scanner) {
        String checkInDateDisplayMessage = "Enter CheckIn Date mm/dd/yyyy in the following format: 02/01/2022";
        String checkOutDateDisplayMessage = "Enter CheckOut Date mm/dd/yyyy in the following format: 02/21/2022";
        Date checkInDate = parseInputStringToDate(checkInDateDisplayMessage, "mm/dd/yyyy", scanner);
        Date checkOutDate = parseInputStringToDate(checkOutDateDisplayMessage, "mm/dd/yyyy", scanner);

        Collection<IRoom> availableRoomCollection = hotelResource.findARoom(checkInDate,
                checkOutDate);
        if(availableRoomCollection.isEmpty()) {
            availableRoomCollection = hotelResource.findARoom(getDateAdd7Days(checkInDate),
                    getDateAdd7Days(checkOutDate));
            String msg = "Sorry, we could not find any room which is available according to the time period you requested. These are the rooms available";
            hotelResource.printAllRooms(availableRoomCollection, msg);
        } else {
            hotelResource.printRooms(availableRoomCollection);
        }

        String bookRoomMessage = "Would you like to book a room? y/n";
        String hasAccountMessage = "Do you have an account with us? y/n";
        if(answerIsYes(bookRoomMessage, scanner)) {
            if(answerIsYes(hasAccountMessage, scanner)) {
                System.out.println("Enter Email in the format: name@domain.com");
                String customerEmail = scanner.nextLine();
                if(!adminResource.customerAlreadyExists(customerEmail)) {
                    System.out.println("It seems you do not have an account with us, " +
                            "please create an account and then try to reserve a room again.");
                    displayMainMenu(scanner);
                } else {
                    Collection<IRoom> roomsAvailable = ReservationService.getInstance().findRooms(checkInDate, checkOutDate);
                    ReservationService.getInstance().printRooms(roomsAvailable); // print all recommended rooms
                    System.out.println("What room number would you like to reserve?");
                    String roomNumber = scanner.nextLine();
                    if(isSelectedRoomNumberValid(availableRoomCollection, roomNumber)) {
                        IRoom selectedRoom = hotelResource.getRoom(roomNumber);
                        hotelResource.bookARoom(customerEmail,
                                selectedRoom, checkInDate, checkOutDate);
                        System.out.println("Room was booked successfully.");
                    } else {
                        System.out.println("The input room number is invalid, reservation failed to process.");
                        displayMainMenu(scanner);
                    }
                }
            } else {
                System.out.println("Please create an account with us first. Thank you.");
            }
        }
        displayMainMenu(scanner);
    }

    private static Date parseInputStringToDate(String askDateStringDisplayMessage,
                                               String patternString,
                                               Scanner scanner) {
        SimpleDateFormat formatter = new SimpleDateFormat(patternString, Locale.ENGLISH);
        System.out.println(askDateStringDisplayMessage);
        String dateInString = null;
        Date date = null;
        while(date == null) {
            try {
                dateInString = scanner.nextLine();
                date = formatter.parse(dateInString);
            } catch (ParseException pe) {
                System.out.println("Input date String = " + dateInString + " is not valid, " +
                        "error = " + pe.getMessage());
                System.out.println(askDateStringDisplayMessage);
            }
        }
        return date;
    }

    private static Date getDateAdd7Days(Date date) {
        long dateTimeStampPlus7Days = date.getTime() + 7*24*60*60*1000;
        return new Date(dateTimeStampPlus7Days);
    }

    private static boolean isSelectedRoomNumberValid(Collection<IRoom> availableRoomCollection,
                                                     String roomNumber) {
        boolean isSelectRoomValidFlag = false;
        for(IRoom eachRoom : availableRoomCollection) {
            if(eachRoom.getRoomNumber().equalsIgnoreCase(roomNumber)) {
                isSelectRoomValidFlag = true;
                break;
            }
        }
        return isSelectRoomValidFlag;
    }

    //This is a more complicated approach:

//    public static void findAndReserveARoom(Scanner scanner) {
//        System.out.println("Please enter your check-in date in MM/DD/YYYY format: ");
//        Date checkInDate = getUserProvidedDate(scanner);
//        System.out.println("Please enter your check-out date in MM/DD/YYYY format: ");
//        Date checkOutDate = getUserProvidedDate(scanner);
//        // find rooms first if not found send alternative ones
//        Collection<IRoom> roomsAvailable = ReservationService.getInstance().findRooms(checkInDate, checkOutDate);
//        if (roomsAvailable.isEmpty()) { // if there are no available rooms
//            Date alternateCheckInDate = ReservationService.getInstance().addSuggestedSevenDaysToOriginalReservation(checkInDate);
//            Date alternateCheckOutDate = ReservationService.getInstance().addSuggestedSevenDaysToOriginalReservation(checkOutDate);
//            roomsAvailable = ReservationService.getInstance().findRooms(alternateCheckInDate, alternateCheckOutDate);
//            System.out.println("No rooms were available. These are the recommendations we found: ");
//            ReservationService.getInstance().printRooms(roomsAvailable); // print all recommended rooms
//        } else { // if we found rooms in the default checkin/out dates
//            ReservationService.getInstance().printRooms(roomsAvailable); // print all recommended rooms
//        }
//        System.out.print("do you want to book a room with us? (Y/N) >");
//        if (isYesOrNo(scanner)) { // we are about to book
//            System.out.print("do you have an account? (Y/N) > ");
//            if (isYesOrNo(scanner)) { // means he has an account
//                System.out.print("enter your email > ");
//                String emailInput = scanner.nextLine();
//                boolean doesCustomerExists = CustomerService.getInstance().isCustomerAlreadyExists(emailInput);
//                if (!doesCustomerExists) { // ask to create an account
//                    System.out.println("you do not have account, create an account first");
//                    displayCreateAccount(scanner);
//                } else { //if there is an account
//                    //display all rooms
//                    System.out.println("=== displaying all rooms ===");
//                    ReservationService.getInstance().printAllRooms();
//                    System.out.print("Enter room number > ");
//                    String roomNumber = scanner.nextLine();
//                    if (checkIfRoomNumberIsValid(roomsAvailable, roomNumber)) { // check if room is valid or not if not it will never throw
//                        if (checkIfThereIsCheckInOnSameDay(checkInDate)) { //want to use check in and check out date if available room
//                            Date alternateCheckInDate = ReservationService.getInstance().addSuggestedSevenDaysToOriginalReservation(checkInDate);
//                            Date alternateCheckOutDate = ReservationService.getInstance().addSuggestedSevenDaysToOriginalReservation(checkOutDate);
//                            roomsAvailable = ReservationService.getInstance().findRooms(alternateCheckInDate, alternateCheckOutDate);
//                            System.out.println("no rooms available but we have these recommended rooms for you");
//                            ReservationService.getInstance().printRooms(roomsAvailable); // print all recommended rooms
//                        }
//                        IRoom roomSelected = HotelResource.getInstance().getRoom(roomNumber);
//                        Customer customer = CustomerService.getInstance().getCustomer(emailInput);
//                        HotelResource.getInstance().bookARoom(emailInput, roomSelected, checkInDate, checkOutDate);
//                    } else {
//                        System.out.println("room number is invalid try again.");
//                    }
//                }
//            } else {
//                System.out.println("create an account first");
//                displayCreateAccount(scanner);
//            }
//            System.out.println();
//        }
//        displayMainMenu(scanner);
//    }

    private static boolean checkIfRoomNumberIsValid(Collection<IRoom> rooms, String roomNumber) {
        boolean isValid = false;
        for (IRoom room : rooms) {
            if (room.getRoomNumber().equals(roomNumber)) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }

    private static boolean checkIfThereIsCheckInOnSameDay(Date checkInDate) {
        boolean isReservedOnTheSameDay = false;
        Collection<Reservation> allReservations = ReservationService.getInstance().getAllReservations();
        for (Reservation eachReservation : allReservations) {
            if (eachReservation.getCheckInDate().equals(checkInDate)) {
                isReservedOnTheSameDay = true;
                break;
            }
        }
        return isReservedOnTheSameDay;
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


    public static void performFindAndReserveARoomChecks(Scanner scanner, String inputEmail, Date checkInDate, Date checkOutDate) {
        Collection<IRoom> roomsAvailable = hotelResource.findARoom(checkInDate, checkOutDate);
        if (roomsAvailable.isEmpty()) { // if no rooms we will recommend/alternate another dates
            Date suggestedCheckIn = hotelResource.addSuggestedSevenDays(checkInDate);
            Date suggestedCheckOut = hotelResource.addSuggestedSevenDays(checkOutDate);
            roomsAvailable = hotelResource.findARoom(suggestedCheckIn, suggestedCheckOut);
            System.out.println("We can not find rooms based on the provided Check-in and Check-out dates. We recommended rooms for another date");
            ReservationService.getInstance().printRooms(roomsAvailable);
        } else { // if there are rooms available we just print them
            ReservationService.getInstance().printRooms(roomsAvailable);
        }
    }


    public static Date getUserProvidedDate(Scanner scanner) {
        try {
            return new SimpleDateFormat(ACCEPTABLE_DATE_FORMAT).parse(scanner.next());
        } catch (ParseException ex) {
            System.out.println("Error: You Entered an Invalid date that is not in MM/DD/YYYY format.");
            findAndReserveARoom(scanner);
        }
        return null;
    }

    private static boolean isYesOrNo(String answerString){
        boolean yesOrNoFlag = false;
        if(answerString != null){
            if("YES".equalsIgnoreCase(answerString) ||
                    "Y".equalsIgnoreCase(answerString) ||
                    "N".equalsIgnoreCase(answerString) ||
                    "NO".equalsIgnoreCase(answerString))
                yesOrNoFlag = true;
        }
        return yesOrNoFlag;
    }


    private static boolean answerIsYes(String questionMessage, Scanner scanner){
        boolean isAnswerYes = false;
        System.out.println(questionMessage);
        String answer = scanner.nextLine();
        while(!isYesOrNo(answer)) {
            System.out.println("Please enter YES (Y) or NO (N)");
            System.out.println(questionMessage);
            answer = scanner.nextLine();
        }
        return isAnswerYes(answer);
    }

    private static boolean isAnswerYes(String answerString){
        boolean isYesFlag = false;
        if(answerString != null){
            if("YES".equalsIgnoreCase(answerString) ||
                    "Y".equalsIgnoreCase(answerString))
                isYesFlag = true;
        }
        return isYesFlag;
    }
}