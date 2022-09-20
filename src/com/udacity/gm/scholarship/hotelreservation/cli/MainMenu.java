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
import java.util.*;

import static com.udacity.gm.scholarship.hotelreservation.cli.AdminMenu.displayAdminMenu;
//import static com.udacity.gm.scholarship.hotelreservation.service.ReservationService.printRooms;

//Reference: Mentor session 7-27-22, Mentor Session 7-30-22, and Mentor Session 7-31-22
public class MainMenu {
    private static final HotelResource hotelResource = HotelResource.getInstance();
    private static final AdminResource adminResource = AdminResource.getInstance();

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

    //diff approach based on Michael's example during the connect session:

    private static void findAndReserveARoom(Scanner scanner) {
        Date checkInDate = parseInputStringToDate("Enter Check In date in mm/dd/yyyy format", scanner);
        Date checkOutDate = parseInputStringToDate("Enter Check Out date in mm/dd/yyyy format", scanner);
        Collection<IRoom> availableRoomCollection = HotelResource.getInstance().findARoom(checkInDate, checkOutDate);
        if (availableRoomCollection.isEmpty()) { // check if there are no rooms available for the date
            Date recommendedCheckInDate = getDateAdd7Days(checkInDate);
            Date recommendedCheckOutDate = getDateAdd7Days(checkOutDate);
            availableRoomCollection = HotelResource.getInstance().findARoom(recommendedCheckInDate, recommendedCheckOutDate);
            System.out.println("Sorry, we can not find available rooms but we have provided recommended rooms");
            HotelResource.getInstance().printRooms(availableRoomCollection);
        } else {
            HotelResource.getInstance().printRooms(availableRoomCollection);
        }


        if (answerIsYes("Do you want to book a room? (y/n)", scanner)) {
            if (answerIsYes("Do you already have an account with us? (y/n)", scanner)) {
                System.out.print("Enter your email > ");
                String emailInput = scanner.nextLine();
                if (!CustomerService.getInstance().customerAlreadyExists(emailInput)) {
                    System.out.print("You do not have account. Please create an account first then try again.");
                    displayCreateAccount(scanner);
                } else {
                    System.out.print("Enter room number >");
                    String roomNumber = scanner.nextLine();
                    if (isSelectedRoomNumberValid(availableRoomCollection, roomNumber)) {
                        if (checkIfThereIsCheckInOnSameDay(checkInDate)) {
                            Date recommendedCheckInDate = getDateAdd7Days(checkInDate);
                            Date recommendedCheckOutDate = getDateAdd7Days(checkOutDate);
                            availableRoomCollection = HotelResource.getInstance().findARoom(recommendedCheckInDate, recommendedCheckOutDate);
                            System.out.println("Sorry the room is reserved on that day, but we have recommended dates for an alternative check in");
                            HotelResource.getInstance().printRooms(availableRoomCollection);
                        }
                        IRoom selectedRoom = HotelResource.getInstance().getRoom(roomNumber);
                        Customer customerWhoMakeReservation = HotelResource.getInstance().getCustomer(emailInput);
                        HotelResource.getInstance().bookARoom(customerWhoMakeReservation.getEmail(), selectedRoom, checkInDate, checkOutDate);
                    } else {
                        System.out.println("The room number is invalid, Please try again...");
                    }

                }
            } else {
                System.out.println("Please create an account first. Thank you");
                displayCreateAccount(scanner);
            }
        }
        displayMainMenu(scanner);
    }

    private static Date parseInputStringToDate(String message, Scanner scanner) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        System.out.print(message + " > ");
        String dateInput = null;
        Date date = null;
        while (date == null) {
            try {
                dateInput = scanner.nextLine();
                date = formatter.parse(dateInput);
            } catch (ParseException ex) {
                System.out.println("input date string " + dateInput + " is not valid," + ex.getMessage());
                System.out.print(message + " > ");
            }
        }
        return date;
    }

    private static Date getDateAdd7Days(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 7); // add 7 days
        return c.getTime();
    }

    private static boolean isSelectedRoomNumberValid(Collection<IRoom> rooms, String roomNumber) {
        boolean isValid = false;
        for (IRoom room : rooms) {
            if (room.getRoomNumber().equals(roomNumber)) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }

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


    public static Date getUserProvidedDate(Scanner scanner) {
        try {
            return new SimpleDateFormat(ACCEPTABLE_DATE_FORMAT).parse(scanner.next());
        } catch (ParseException ex) {
            System.out.println("Error: You Entered an Invalid date that is not in MM/DD/YYYY format.");
            findAndReserveARoom(scanner);
        }
        return null;
    }

    private static boolean isYesOrNo(String answerString) {
        boolean yesOrNoFlag = false;
        if (answerString != null) {
            if ("YES".equalsIgnoreCase(answerString) ||
                    "Y".equalsIgnoreCase(answerString) ||
                    "N".equalsIgnoreCase(answerString) ||
                    "NO".equalsIgnoreCase(answerString))
                yesOrNoFlag = true;
        }
        return yesOrNoFlag;
    }


    private static boolean answerIsYes(String questionMessage, Scanner scanner) {
        boolean isAnswerYes = false;
        System.out.println(questionMessage);
        String answer = scanner.nextLine();
        while (!isYesOrNo(answer)) {
            System.out.println("Please enter YES (Y) or NO (N)");
            System.out.println(questionMessage);
            answer = scanner.nextLine();
        }
        return isAnswerYes(answer);
    }

    private static boolean isAnswerYes(String answerString) {
        boolean isYesFlag = false;
        if (answerString != null) {
            if ("YES".equalsIgnoreCase(answerString) ||
                    "Y".equalsIgnoreCase(answerString))
                isYesFlag = true;
        }
        return isYesFlag;
    }
}
