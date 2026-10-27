
import java.util.ArrayList;

public class Hotel {
    private ArrayList<Room> rooms;
    private ArrayList<Booking> bookings;
    private int nextBookingId;


    public Hotel() {
        this.rooms = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.nextBookingId = 1001; // Starting ID for bookings
        initializeRooms();
    }

    // Initialize sample hotel rooms
    private void initializeRooms() {
        // Standard rooms
        rooms.add(new StandardRoom(101, 50.0));
        rooms.add(new StandardRoom(102, 50.0));
        rooms.add(new StandardRoom(103, 60.0));

        // Deluxe rooms
        rooms.add(new DeluxeRoom(201, 100.0));
        rooms.add(new DeluxeRoom(202, 120.0));
    }

    // Display all rooms in the hotel
    public void displayAllRooms() {
        System.out.println("\n========== All Hotel Rooms ==========");
        for (Room room : rooms) {
            room.displayRoomDetails();
        }
        System.out.println("=====================================");
    }

    // Display only currently available rooms
    public void displayAvailableRooms() {
        System.out.println("\n======= Available Hotel Rooms =======");
        boolean anyAvailable = false;
        for (Room room : rooms) {
            if (room.isAvailable()) {
                room.displayRoomDetails();
                anyAvailable = true;
            }
        }
        if (!anyAvailable) {
            System.out.println("No rooms are currently available.");
        }
        System.out.println("=====================================");
    }

    // Find a room by room number
    public Room findRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    // Create a new booking
    public Booking createBooking(Guest guest, int roomNumber, int nights) {
        Room room = findRoomByNumber(roomNumber);

        // Validation 1: Room existence
        if (room == null) {
            System.out.println("\n[Error] Room number " + roomNumber + " does not exist.");
            return null;
        }

        // Validation 2: Room availability
        if (!room.isAvailable()) {
            System.out.println("\n[Error] Room " + roomNumber + " is already occupied. Cannot book.");
            return null;
        }

        // Validation 3: Valid duration
        if (nights <= 0) {
            System.out.println("\n[Error] Number of nights must be at least 1.");
            return null;
        }

        // Create booking and mark room as occupied
        Booking booking = new Booking(nextBookingId++, guest, room, nights);
        room.setAvailable(false);
        bookings.add(booking);
        FileHandler.saveBooking(booking);

        System.out.println("\n[Success] Room booked successfully!");
        booking.displayBookingDetails();
        return booking;
    }

    // Display all bookings
    public void displayAllBookings() {
        System.out.println("\n========== Hotel Bookings ==========");
        if (bookings.isEmpty()) {
            System.out.println("No bookings recorded yet.");
        } else {
            for (Booking booking : bookings) {
                booking.displayBookingDetails();
            }
        }
        System.out.println("====================================");
    }

    // Find an active booking by booking ID
    public Booking findActiveBooking(int bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId() == bookingId && booking.isActive()) {
                return booking;
            }
        }
        return null;
    }

    // Find any booking (active or completed) by booking ID
    public Booking findBookingById(int bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId() == bookingId) {
                return booking;
            }
        }
        return null;
    }

    // Check out guest and release room
    public boolean checkOutGuest(int bookingId) {
        Booking booking = findBookingById(bookingId);

        if (booking == null) {
            System.out.println("\n[Error] Booking ID #" + bookingId + " not found.");
            return false;
        }

        if (!booking.isActive()) {
            System.out.println("\n[Error] Booking ID #" + bookingId + " has already been checked out.");
            return false;
        }

        // Process checkout
        booking.checkOut();
        FileHandler.saveAllBookings(bookings);
        System.out.println("\n[Success] Guest checked out successfully!");
        System.out.println("Room #" + booking.getRoom().getRoomNumber() + " is now available for new bookings.");
        System.out.printf("Total Settled Bill: $%.2f%n", booking.getTotalAmount());
        return true;
    }

    // ========================================================
    // Integration Methods for Team Member 2 (FileHandler)
    // ========================================================

    // Get list of all rooms
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    // Get list of all bookings
    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    // Add a reconstructed booking loaded from file and sync room availability
    public void addReconstructedBooking(Booking booking) {
        if (booking != null) {
            Room hotelRoom = findRoomByNumber(booking.getRoom().getRoomNumber());
            if (hotelRoom != null) {
                booking.setRoom(hotelRoom);
                if (booking.isActive()) {
                    hotelRoom.setAvailable(false);
                }
            } else if (booking.isActive()) {
                booking.getRoom().setAvailable(false);
            }
            bookings.add(booking);
            // Ensure nextBookingId is greater than loaded booking ID
            if (booking.getBookingId() >= nextBookingId) {
                nextBookingId = booking.getBookingId() + 1;
            }
        }
    }
}
