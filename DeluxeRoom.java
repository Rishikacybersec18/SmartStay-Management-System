// SmartStay Hotel Management System
// DeluxeRoom.java - Represents deluxe hotel room extending Room

public class DeluxeRoom extends Room {
    // Deluxe rooms include complimentary luxury amenities and premium service charge (15%)
    private static final double LUXURY_SURCHARGE_PERCENT = 0.15;

    // Constructor calling superclass constructor
    public DeluxeRoom(int roomNumber, double pricePerNight) {
        super(roomNumber, pricePerNight);
    }

    // Overriding abstract method from Room
    @Override
    public String getRoomType() {
        return "Deluxe";
    }

    // Overriding calculateBill from Billable interface
    // Demonstrates runtime polymorphism: applies a 15% luxury surcharge
    @Override
    public double calculateBill(int nights) {
        double basePrice = getPricePerNight() * nights;
        return basePrice + (basePrice * LUXURY_SURCHARGE_PERCENT);
    }
}
