package com.udacity.gm.scholarship.hotelreservation.service;

import com.udacity.gm.scholarship.hotelreservation.model.Customer;
import com.udacity.gm.scholarship.hotelreservation.model.IRoom;
import com.udacity.gm.scholarship.hotelreservation.model.Reservation;

import java.util.*;

//Reference: Mentor session 7-27-22, Mentor Session 7-30-22, and Mentor Session 7-31-22
public class ReservationService {
    private ReservationService() {
    }

    private static ReservationService RESERVATION_SERVICE_INSTANCE = null;

    //Source: https://www.geeksforgeeks.org/singleton-class-java/
    public static ReservationService getInstance() {
        if (RESERVATION_SERVICE_INSTANCE == null) {
            RESERVATION_SERVICE_INSTANCE = new ReservationService();
        }
        return RESERVATION_SERVICE_INSTANCE;
    }

    private static final Map<String, IRoom> roomsMap = new HashMap<>();

    private static final Collection<Reservation> reservations = new ArrayList<>();

    public void addRoom(IRoom room) {
        String roomNumber = room.getRoomNumber();
        if (!roomCurrentlyExists(roomNumber))
            roomsMap.put(roomNumber, room);
    }

    boolean roomCurrentlyExists(String roomNumber) {
        boolean roomCurrentlyExistsFlag = false;
        IRoom room = roomsMap.get(roomNumber);
        if (room != null)
            roomCurrentlyExistsFlag = true;
        return roomCurrentlyExistsFlag;
    }

    public IRoom getARoom(String roomId) {
        return roomsMap.get(roomId);
    }

    public Collection<IRoom> getAllRooms() {
        return roomsMap.values();
    }

    public Reservation reserveARoom(Customer customer, IRoom room,
                                    Date checkInDate, Date checkOutDate) {
        Collection<IRoom> availableRooms = findRooms(checkInDate, checkOutDate);
        Reservation reservation = null;
        if (requestedRoomAvailable(availableRooms, room)) {
            reservation = new Reservation(customer, room, checkInDate, checkOutDate);
            reservations.add(reservation);
        } else {
            System.out.println("I was not able to find available rooms");
        }
        return reservation;
    }

    private boolean requestedRoomAvailable(Collection<IRoom> availableRooms, IRoom room) {
        boolean isRequestedRoomAvailableFlag = false;
        for (IRoom eachRoom : availableRooms) {
            if (eachRoom.getRoomNumber().equalsIgnoreCase(room.getRoomNumber())) {
                isRequestedRoomAvailableFlag = true;
                break;
            }
        }
        return isRequestedRoomAvailableFlag;
    }

    private boolean requestedRoomHasConflictWithExistingReservation(IRoom room, Date checkInDate, Date checkOutDate) {
        boolean hasConflict = false;
        for (Reservation reservation : reservations) {
            if (reservationWithTheSameRoom(reservation, room)) {
                if (reservationHasConflict(reservation, checkInDate, checkOutDate)) {
                    hasConflict = true;
                    break;
                }
            }
        }
        return hasConflict;
    }

    private boolean reservationHasConflict(Reservation reservation, Date checkInDate, Date checkOutDate) {
        Date reservationCheckInDate = reservation.getCheckInDate();
        Date reservationCheckOutDate = reservation.getCheckOutDate();
        return checkInDate.before(reservationCheckInDate) &&
                checkOutDate.before(reservationCheckInDate) ||
                checkInDate.after(reservationCheckOutDate) && checkOutDate.after(reservationCheckOutDate);
    }

    private boolean requestCheckInCheckOutDatesInsideReservationCheckInCheckOutDates(Reservation reservation, Date checkInDate, Date checkOutDate) {
        return false;
    }

    private boolean reservationCheckInCheckOutDatesInsideRequestedCheckInCheckOutDates(Reservation reservation, Date checkInDate, Date checkOutDate) {
        boolean reservationCheckInCheckOutDatesInsideFlag = false;
        return reservationCheckInCheckOutDatesInsideFlag;
    }

    private boolean reservationWithTheSameRoom(Reservation reservation, IRoom room) {
        boolean sameRoomFlag = reservation.getRoom().getRoomNumber().equalsIgnoreCase(room.getRoomNumber());
        return sameRoomFlag;
    }

    //new method suggested by reviewer:
    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        return getAvailableRooms(checkInDate, checkOutDate);
    }

    public Collection<IRoom> getAvailableRooms(Date checkInDate, Date checkOutDate) {
        Collection<Reservation> reservations = getAllReservations();
        Collection<IRoom> allRooms = getAllRooms();
        Collection<IRoom> availableRooms = new ArrayList<>();

        // if no reservation has been made, all rooms are available
        if (reservations.isEmpty()) {
            return allRooms;
        }

        for (IRoom room: allRooms) {
            // if rooms is not reserved, add it to vailable rooms
            if (isRoomFree(room, checkInDate, checkOutDate)) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    //new code suggested by reviewer:
    private boolean isRoomFree(IRoom room, Date checkIn, Date checkOut) {
        Collection<Reservation> reservations = getAllReservations();
        boolean isFree = true;
        for (Reservation reservation: reservations) {
            IRoom reservedRoom = reservation.getRoom();
            // if this room has been reserved, also check if dates conflict
            if (reservedRoom.getRoomNumber().equals(room.getRoomNumber())) {
                isFree = false;
                if (!reservationConflictsWithDateRange(reservation, checkIn, checkOut)) {
                    // if the reservation does not conflict with the range, it means the room has been reserved
                    return true;
                }
            }
        }

        // if we pass though the loop successfully, then the room has not been reserved
        // because it does not conflict with any reserved room
        // see how breaking code into descriptive, small chunks makes it easy?
        return isFree;
    }

    //new code suggested by reviewer:
    private boolean reservationConflictsWithDateRange(Reservation reservation, Date checkin, Date checkout) {
        // Please,see explanation in the sections below
        // the room is free is you check into the room after the other person has checked out
        // or you check out  of the room before then person checks in
        boolean reservationDoesNotConflict = checkout.before(reservation.getCheckInDate()) || checkin.after(reservation.getCheckOutDate());
        return !(reservationDoesNotConflict);
    }

    public boolean areDatesReservedAlready(Reservation reservation, Date checkInDate, Date checkOutDate) {
//        if (checkInDate.before(reservation.getCheckInDate()) && checkOutDate.after(reservation.getCheckOutDate())) {
        return checkInDate.after(reservation.getCheckInDate()) && checkOutDate.before(reservation.getCheckOutDate());
    }

    public boolean checkIfCheckInDateIsValid(Reservation reservation, Date checkInDate) {
        return (checkInDate.after(reservation.getCheckInDate()) && checkInDate.before(reservation.getCheckInDate()));
    }

    private boolean roomHasNoConflict(List<String> conflictRoomNumberList, String roomNumber) {
        boolean noConflictFlag = true;
        for (String eachRoomNumber : conflictRoomNumberList) {
            if (eachRoomNumber.equalsIgnoreCase(roomNumber)) {
                noConflictFlag = false;
                break;
            }
        }
        return noConflictFlag;
    }

    private List<String> getConflictingRoomNumbers(Collection<Reservation> reservations,
                                                   Date checkInDate, Date checkOutDate) {
        List<String> conflictRoomNumberList = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservationHasConflictWithCheckInCheckOutDates(reservation,
                    checkInDate, checkOutDate))
                conflictRoomNumberList.add(reservation.getRoom().getRoomNumber());
        }
        return conflictRoomNumberList;
    }

    private boolean reservationHasConflictWithCheckInCheckOutDates(Reservation reservation,
                                                                   Date checkInDate, Date checkOutDate) {
        return targetDateBetweenReservationCheckInOutDates(checkInDate, reservation) ||
                targetDateBetweenReservationCheckInOutDates(checkOutDate, reservation) ||
                reservationCheckInOutDatesWithinCheckInDateAndCheckOutDate(checkInDate, checkOutDate, reservation);
    }

    private boolean targetDateBetweenReservationCheckInOutDates(Date checkInDate, Reservation reservation) {
        return checkInDate.compareTo(reservation.getCheckInDate()) > 0 &&
                checkInDate.compareTo(reservation.getCheckOutDate()) < 0;
    }

    private boolean reservationCheckInOutDatesWithinCheckInDateAndCheckOutDate(Date checkInDate, Date checkOutDate, Reservation reservation) {
        return checkInDate.compareTo(reservation.getCheckInDate()) < 0 &&
                checkOutDate.compareTo(reservation.getCheckOutDate()) > 0;
    }

    private boolean reservationCheckInOutDateConflictWithRequestCheckInOutDate(Reservation reservation,
                                                                               Date checkInDate, Date checkOutDate) {
        boolean hasConflict = reservation.getCheckInDate().before(checkOutDate)
                && reservation.getCheckOutDate().after(checkInDate);
        return hasConflict;
    }

    private boolean reservationCheckInOutDateSameWithRequestedCheckInOutDate(Reservation reservation,
                                                                             Date checkInDate, Date checkOutDate) {
        boolean isSameFlag = reservation.getCheckOutDate().compareTo(checkInDate) == 0
                && reservation.getCheckOutDate().compareTo(checkOutDate) == 0;
        return isSameFlag;
    }

    private boolean reservationCheckInOutDateConflictWithRequestedCheckInOutDate(Reservation reservation,
                                                                                 Date checkInDate, Date checkOutDate) {
        boolean hasConflict = reservation.getCheckInDate().before(checkOutDate)
                && reservation.getCheckOutDate().after(checkInDate);
        return hasConflict;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationService)) return false;
        ReservationService that = (ReservationService) o;
        return Objects.equals(reservations, reservations) && Objects.equals(roomsMap, roomsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservations, roomsMap);
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        List<Reservation> customerReservationList = new ArrayList<>();
        String customerEmail = customer.getEmail();
        for (Reservation reservation : reservations) {
            if (isReservationBelongToCustomer(reservation, customerEmail))
                customerReservationList.add(reservation);
        }
        return customerReservationList;
    }

    private boolean isReservationBelongToCustomer(Reservation reservation, String customerEmail) {
        boolean doesReservationBelongCustomer = false;
        String reservationCustomerEmail = reservation.getCustomer().getEmail();
        if (reservationCustomerEmail.equalsIgnoreCase(customerEmail))
            doesReservationBelongCustomer = true;
        return doesReservationBelongCustomer;
    }

    public void printAllReservations() {
        for (Reservation eachReservation : reservations) {
            System.out.println(eachReservation);
        }
//        System.out.println(reservations);
    }


    public void printReservations(Collection<Reservation> reservations) {
        int index = 1;
        for (Reservation reservation : reservations) {
            System.out.println("Reservation No." + index + ":");
            System.out.println("\t" + reservation);
            System.out.println();
            index++;
        }
    }

    //this is printing rooms from the Hash Map from above.
    public void printAllRooms() {
        int index = 1;
        for (IRoom room : roomsMap.values()) {
            //System.out.println("Room Index:" + index + ":");
            System.out.println("\t Room Number: " + room.getRoomNumber());
            System.out.println("\t Room Price: " + room.getRoomPrice());
            System.out.println("\t Room Type: " + room.getRoomType());
            System.out.println("\t Is The Room Free: " + room.isFree());
            System.out.println();
            index++;
        }
    }

    //this is to print specific rooms based on the parameter passed in.
    public void printRooms(Collection<IRoom> rooms) {
        int index = 1;
        for (IRoom room : rooms) {
            System.out.println("Room Number: " + room.getRoomNumber());
            System.out.println("\t Room Price: " + room.getRoomPrice());
            System.out.println("\t Room Type: " + room.getRoomType());
            System.out.println("\t Is The Room Free: " + room.isFree());
            System.out.println();
            index++;
        }
    }

    public Collection<Reservation> getAllReservations() {
        return reservations;
    }

    public Collection<IRoom> findSuggestedRooms(Date checkInDate, Date checkOutDate) {
        return findRooms(addSuggestedSevenDaysToOriginalReservation(checkInDate),
                addSuggestedSevenDaysToOriginalReservation(checkOutDate));
    }

    //source: https://www.tabnine.com/code/java/methods/java.util.Calendar/set  &
    //https://www.geeksforgeeks.org/calendar-set-method-in-java-with-examples/
    public Date addSuggestedSevenDaysToOriginalReservation(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 7);
        return calendar.getTime();
    }

    //michael's suggestion:
    private static Date getWeekAfter(Date date) {
        long datePlus7Days = date.getTime() + 7 * 24 * 60 * 1000;
        return new Date(datePlus7Days);
    }
}
