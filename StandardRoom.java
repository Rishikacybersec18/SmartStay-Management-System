
public class StandardRoom extends Room {
    public StandardRoom(int roomNumber, double pricePerNight) {
        super(roomNumber, pricePerNight);
    }

    // Overriding abstract method from Room
    @Override
    public String getRoomType() {
        return "Standard";
    }

    // Overriding calculateBill from Billable interface
    // Standard rooms have standard pricing: pricePerNight * nights
    @Override
    public double calculateBill(int nights) {
        return getPricePerNight() * nights;
    }
}
