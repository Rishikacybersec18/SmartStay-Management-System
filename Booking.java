
public class Booking {
    private int bookingId;
    private Guest guest;
    private Room room;
    private int nights;
    private double totalAmount;
    private String status; // "ACTIVE" or "CHECKED_OUT"


    public Booking(int bookingId, Guest guest, Room room, int nights) {
        this.bookingId = bookingId;
        this.guest = guest;
        this.room = room;
        this.nights = nights;
        this.totalAmount = room.calculateBill(nights);
        this.status = "ACTIVE";
    }

    // Overloaded Constructor for reconstructing bookings from file storage (Used by Team Member 2)
    public Booking(int bookingId, Guest guest, Room room, int nights, double totalAmount, String status) {
        this.bookingId = bookingId;
        this.guest = guest;
        this.room = room;
        this.nights = nights;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getBookingId() {
        return bookingId;
    }

    public Guest getGuest() {
        return guest;
    }

    public Room getRoom() {
        return room;
    }

    public int getNights() {
        return nights;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    // Check if the booking is currently active
    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.status);
    }

    public void checkOut() {
        this.status = "CHECKED_OUT";
        this.room.setAvailable(true);
    }


    public void displayBookingDetails() {
        System.out.println("Booking ID     : #" + bookingId);
        System.out.println("Status         : " + status);
        System.out.println("Guest Name     : " + guest.getName() + " (ID: " + guest.getGuestId() + ")");
        System.out.println("Contact Number : " + guest.getContactNumber());
        System.out.println("Room Number    : " + room.getRoomNumber() + " (" + room.getRoomType() + " Room)");
        System.out.println("Price per Night: $" + String.format("%.2f", room.getPricePerNight()));
        System.out.println("Nights Stayed  : " + nights);
        System.out.println("Total Bill     : $" + String.format("%.2f", totalAmount));
        System.out.println("----------------------------------------");
    }
}
public String toCsv() {
    // Sanitise string tokens to prevent standard CSV delimiter collision
    String safeGuestName = (this.guest != null && this.guest.getName() != null) 
            ? this.guest.getName().replace(",", " ") 
            : "Unknown";
    
    String safeGuestId = (this.guest != null) ? this.guest.getGuestId() : "N/A";
    String safeRoomNumber = (this.room != null) ? this.room.getRoomNumber() : "N/A";
    String roomTypeCode = (this.room instanceof DeluxeRoom) ? "DeluxeRoom" : "StandardRoom";

    return String.format("%s,%s,%s,%s,%s,%d,%.2f",
            this.bookingId,
            safeGuestId,
            safeGuestName,
            safeRoomNumber,
            roomTypeCode,
            this.durationNights,
            this.totalBill);
}

/**
 * Static factory method parsing a single CSV record and instantiating a fully populated Booking object.
 * Bubble up NumberFormatException or IllegalArgumentException if string formatting is invalid.
 *
 * @param csvLine Raw comma-separated string read from persistent file storage.
 * @return Fully rehydrated Booking instance containing linked Guest and Room subclass entities.
 * @throws IllegalArgumentException If csvLine is null, blank, or missing required token length.
 * @throws NumberFormatException If durationNights or totalBill tokens contain malformed numeric data.
 */
public static Booking fromCsv(String csvLine) {
    if (csvLine == null || csvLine.trim().isEmpty()) {
        throw new IllegalArgumentException("CSV record line is null or empty.");
    }

    String[] tokens = csvLine.split(",");
    if (tokens.length < 7) {
        throw new IllegalArgumentException("Insufficient attribute tokens. Expected 7, found: " + tokens.length);
    }

    // 1. Extraction & Whitespace Trimming
    String bookingId = tokens[0].trim();
    String guestId = tokens[1].trim();
    String guestName = tokens[2].trim();
    String roomNumber = tokens[3].trim();
    String roomType = tokens[4].trim();

    // 2. Numeric Conversions (Bubbles NumberFormatException if parsing fails)
    int durationNights = Integer.parseInt(tokens[5].trim());
    double totalBill = Double.parseDouble(tokens[6].trim());

    // 3. Domain Entity Instantiation
    Guest guest = new Guest(guestId, guestName);
    
    Room room;
    if ("DeluxeRoom".equalsIgnoreCase(roomType)) {
        room = new DeluxeRoom(roomNumber);
    } else {
        room = new StandardRoom(roomNumber);
    }

    // Rehydrate state and retain calculated liability balance
    Booking booking = new Booking(bookingId, guest, room, durationNights);
    booking.setTotalBill(totalBill);

    return booking;
}
