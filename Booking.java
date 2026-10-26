
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
