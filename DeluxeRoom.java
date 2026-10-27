
public class DeluxeRoom extends Room {
    private static final double LUXURY_SURCHARGE_PERCENT = 0.15;

    public DeluxeRoom(int roomNumber, double pricePerNight) {
        super(roomNumber, pricePerNight);
    }

    public DeluxeRoom(int roomNumber) {
        super(roomNumber, 100.0);
    }

    // Overriding abstract method from Room
    @Override
    public String getRoomType() {
        return "Deluxe";
    }

    @Override
    public double calculateBill(int nights) {
        double basePrice = getPricePerNight() * nights;
        return basePrice + (basePrice * LUXURY_SURCHARGE_PERCENT);
    }
}
