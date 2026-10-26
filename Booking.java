
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
