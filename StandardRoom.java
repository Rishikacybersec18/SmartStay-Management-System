
public class StandardRoom extends Room {
    public StandardRoom(int roomNumber, double pricePerNight) {
        super(roomNumber, pricePerNight);
    }

    public StandardRoom(int roomNumber) {
        super(roomNumber, 50.0);
    }

    // Overriding abstract method from Room
    @Override
    public String getRoomType() {
        return "Standard";
    }

    @Override
    public double calculateBill(int nights) {
        return getPricePerNight() * nights;
    }
}
