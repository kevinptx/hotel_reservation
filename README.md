# hotel_reservation is a Java project for hotel reservation management in order to demonstrate advanced Java programming skills.

This is a `java` application for booking hotel rooms which I developed for the Udacity's [Java Programming Course](https://www.udacity.com/course/java-programming-nanodegree--nd079).
According to the above Udacity website, "This Nanodegree program is intended to elevate your Java abilities and teach you the skills used by Java developers to design, code, test and deploy cutting-edge Java software. 
It is ideal for intermediate programmers with little Java experience who want to set themselves up for success as a professional Java developer and deploy functional Java-based applications of their own creation."

The objective of the hotel_reservation project was to code a hotel application that allows users to book and manage room reservations in a hotel.
The project utilizes the Java programming language to allow one to increase their Java development skills to an advanced level.

The project may be run by executing the [hotel_reservation](https://github.com/kevinptx/hotel_reservation/blob/main/out/production/hotel_reservation/com/udacity/gm/scholarship/hotelreservation/HotelReservationApp.class) class
on your favorite Java IDEA.

<img src="https://github.com/kevinptx/hotel_reservation/blob/main/hotel_reservation.gif" width="500" height="500"/>

![hotel_reservation](https://github.com/kevinptx/hotel_reservation/blob/main/hotel_reservation.gif)

## User Scenarios
The application provides four user scenarios:

1. <b>Creating a customer account</b>: The user needs to create a customer account before creating a room reservation.

2. <b>Searching for rooms</b>: The app should allow the user to search for available rooms based on provided checkin and checkout dates. If the application has available rooms for the specified date range, a list of the corresponding rooms will be displayed to the user for choosing.

3. <b>Booking a room</b>: Once the user has chosen a room, the app will allow them to book the room and create a reservation.

4. <b>Viewing reservations</b>: After booking a room, the app allows customers to view a list of all their reservations.

## Admin Scenarios
The application provides four administrative scenarios:

1. <b>Displaying all customers accounts</b>.
2. <b>Viewing all of the rooms in the hotel</b>.
3. <b>Viewing all of the hotel reservations</b>.
4. <b>Adding a room to the hotel application</b>.

## Reserving a Room
The application allows customers to reserve a room. Here are the details:

1. <b>Avoid conflicting reservations</b>: A single room may only be reserved by a single customer per a checkin and checkout date range.
2. <b>Search for recommended rooms</b>: If there are no available rooms for the customer's date range, a search will be performed that displays recommended rooms on alternative dates. The recommended room search will add seven days to the original checkin and checkout dates to see if the hotel has any availabilities. The recommended rooms/dates will then be displayed to the customer.
> Example: If the customers provided date range search is 02/01/2023 – 02/03/2023 and if all rooms are booked at those specific dates, the system will search again for recommended rooms using the date range 02/08/2023 - 02/10/2023. If there are no recommended rooms available, the system will not return any rooms.

### Room Requirements
<b>Room cost</b>: Rooms will contain a price per night. When displaying rooms, paid rooms will display the price per night and free rooms will display "Free" or have a $0 price.
<b>Unique room numbers</b>: Each room will have a unique room number, meaning that no two rooms can have the same room number.
<b>Room type</b>: Rooms can be either single occupant or double occupant (Enumeration: SINGLE, DOUBLE).

### Customer Requirements
The application will have customer accounts. Each account has:

1. <b>A unique email for the customer</b>: RegEx is used to check that the email is in the correct format (i.e., name@domain.com).
2. <b>A first name and last name</b>.
3. The email RegEx utilized for the purpose of this app is basic for the specific purpose of this project's requirements, and it may not cover all valid email formats observed globally. 
> For example "doej@theaustralian.com.au" would not be accepted by the RegEx this project utilizes because it ends with ".au" instead of ".com". 
Creating RegEx for the above example scenario was not a requirement for this project. 

### Error Requirements
The hotel reservation application handles all exceptions gracefully (user inputs included), meaning:

1. <b>No crashing</b>: The application does not crash based on user input.
2. <b>No unhandled exceptions</b>: The app has try and catch blocks that are used to capture exceptions and provide useful information to the user. There are no unhandled exceptions.
