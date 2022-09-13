package com.udacity.gm.scholarship.hotelreservation.service;

import com.udacity.gm.scholarship.hotelreservation.model.Customer;
import com.udacity.gm.scholarship.hotelreservation.model.IRoom;
import com.udacity.gm.scholarship.hotelreservation.model.Reservation;

import java.util.*;
import java.util.stream.Collectors;

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
    //private Collection<Reservation> reservations = new ArrayList<>();

    private static Map<String, IRoom> roomsMap = new HashMap<>();

    //    private static Collection<Reservation> reservations = new HashSet<>();
    private static Collection<Reservation> reservations = new ArrayList<>();

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
        IRoom room = null;
        for (Map.Entry<String, IRoom> singleRoom : roomsMap.entrySet()) {
            if (singleRoom.getKey().equals(roomId)) {
                room = singleRoom.getValue();
            }
        }
        return room;
    }

    public Collection<IRoom> getAllRooms() {
        return roomsMap.values();
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = null;
        if (!requestedRoomHasConflictWithExistingReservation(room, checkInDate, checkOutDate)) {
            reservation = new Reservation(customer, room, checkInDate, checkOutDate);
            reservations.add(reservation);
        }
        return reservation;
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
        boolean hasConflict = false;
        if (targetDateBetweenReservationCheckInCheckOutDates(checkInDate, reservation) ||
                targetDateBetweenReservationCheckInCheckOutDates(checkOutDate, reservation) ||
                reservationCheckInCheckOutDatesInsideRequestedCheckInCheckOutDates(reservation, checkInDate, checkOutDate) ||
                requestCheckInCheckOutDatesInsideReservationCheckInCheckOutDates(reservation, checkInDate, checkOutDate)) {
            hasConflict = true;
        }
        return hasConflict;
    }

    private boolean requestCheckInCheckOutDatesInsideReservationCheckInCheckOutDates(Reservation reservation, Date checkInDate, Date checkOutDate) {
        return false;
    }

    private boolean reservationCheckInCheckOutDatesInsideRequestedCheckInCheckOutDates(Reservation reservation, Date checkInDate, Date checkOutDate) {
        boolean reservationCheckInCheckOutDatesInsideFlag = false;
        return reservationCheckInCheckOutDatesInsideFlag;
    }

    private boolean targetDateBetweenReservationCheckInCheckOutDates(Date targetDate, Reservation reservation) {
        boolean isBetweenTrueFlag = false;
        if (targetDate.compareTo(reservation.getCheckInDate()) > 0 && targetDate.compareTo(reservation.getCheckOutDate()) < 0)
            isBetweenTrueFlag = true;
        return isBetweenTrueFlag;
    }

    private boolean reservationWithTheSameRoom(Reservation reservation, IRoom room) {
        boolean sameRoomFlag = false;
        if (reservation.getRoom().getRoomNumber().equalsIgnoreCase(room.getRoomNumber()))
            sameRoomFlag = true;
        return sameRoomFlag;
    }

//    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
//        List<String> conflictingRoomNumberList = getConflictingRoomNumbers(this.reservations, checkInDate, checkOutDate);
//        List<IRoom> availableRoomList = new ArrayList<>();
//        Iterator<Map.Entry<String, IRoom>> itr = roomsMap.entrySet().iterator();
//        while (itr.hasNext()) {
//            Map.Entry<String, IRoom> entry = itr.next();
//            String roomNumber = entry.getKey();
//            if (roomHasNoConflict(conflictingRoomNumberList, roomNumber))
//                availableRoomList.add(entry.getValue());
//        }
//        return availableRoomList;
//    }

//    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
//        Collection<IRoom> rooms = getAllRooms();
//        for (Reservation reservation : reservations) {
//            if (checkInDate.before(reservation.getCheckOutDate()) || checkOutDate.after(reservation.getCheckInDate())) {
//                rooms.remove(reservation.getRoom());
//            }
//        }
//        return rooms.stream().collect(Collectors.toList());
//    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        List<String> conflictRoomNumberList = getConflictingRoomNumbers(reservations, checkInDate, checkOutDate);
        List<IRoom> availableRoomList = new ArrayList<>();
        for (Map.Entry<String, IRoom> entry : roomsMap.entrySet()) {
            String roomNumber = entry.getKey();
            if (roomHasNoConflict(conflictRoomNumberList, roomNumber)) {
                availableRoomList.add(entry.getValue());
            }
        }
        return availableRoomList;
    }


    public boolean areDatesReservedAlready(Reservation reservation, Date checkInDate, Date checkOutDate) {
        if (checkInDate.before(reservation.getCheckInDate()) && checkOutDate.after(reservation.getCheckOutDate())) {
            return true;
        } else {
            return false;
        }
    }

    private List<String> getConflictingRoomNumbers(Collection<Reservation> reservations, Date checkInDate, Date checkOutDate) {
        List<String> conflictRoomNumberList = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservationHasConflictWithCheckInCheckOutDates(reservation, checkInDate, checkOutDate)) {
                conflictRoomNumberList.add(reservation.getRoom().getRoomNumber());
            }
        }
        return conflictRoomNumberList;
    }

    private boolean reservationHasConflictWithCheckInCheckOutDates(Reservation reservation, Date checkInDate, Date checkOutDate) {
        boolean hasConflict = false;
        if (reservation.getCheckInDate().equals(checkInDate) && reservation.getCheckOutDate().equals(checkOutDate)) {
            hasConflict = true;
        }
        return hasConflict;
    }

    private boolean roomHasNoConflict(List<String> conflictingRoomNumberList, String roomNumber) {
        boolean noConflictFlag = true;
        for (String eachRoomNumber : conflictingRoomNumberList) {
            if (eachRoomNumber.equalsIgnoreCase(roomNumber))
                noConflictFlag = false;
            break;
        }
        return noConflictFlag;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReservationService)) return false;
        ReservationService that = (ReservationService) o;
        return Objects.equals(reservations, that.reservations) && Objects.equals(roomsMap, that.roomsMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservations, roomsMap);
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        Collection<Reservation> returnedReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getCustomer().equals(customer)) {
                returnedReservations.add(reservation);
            }
        }
        return returnedReservations;
    }

    public void printAllReservations() {
        System.out.println(reservations);
    }


//    public void printAllReservations(){
//        int index = 1;
//        for(Reservation reservation : reservations){
//            System.out.println("Reservation No." + index + ":");
//            System.out.println("\t" + reservation);
//            System.out.println("");
//            index++;
//        }
//    }

    public void printReservations(Collection<Reservation> reservations) {
        int index = 1;
        for (Reservation reservation : reservations) {
            System.out.println("Reservation No." + index + ":");
            System.out.println("\t" + reservation);
            System.out.println("");
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
            System.out.println("");
            index++;
        }
    }

    //this is to print specific rooms based on the parameter passed in.
    public void printRooms(Collection<IRoom> rooms) {
        int index = 1;
        for (IRoom room : roomsMap.values()) {
            System.out.println("Room Number" + room.getRoomNumber());
            System.out.println("\t Room Price: " + room.getRoomPrice());
            System.out.println("\t Room Type: " + room.getRoomType());
            System.out.println("\t Is The Room Free: " + room.isFree());
            System.out.println("");
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
}