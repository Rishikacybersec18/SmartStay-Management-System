
public abstract class Room implements Billable {
    private int roomNumber;
    private double pricePerNight;
    private boolean available;

    public Room(int roomNumber, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.pricePerNight = pricePerNight;
        this.available = true; // By default, newly added rooms are available
    }

    // Getters and Setters
    public int getRoomNumber() {
        return roomNumber;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Abstract method to be implemented by specific room types
    public abstract String getRoomType();

    // Display room details
    public void displayRoomDetails() {
        System.out.printf("Room Number : %-5d | Type: %-10s | Price/Night: $%-7.2f | Status: %s%n",
                roomNumber, getRoomType(), pricePerNight, (available ? "Available" : "Occupied"));
    }
}
