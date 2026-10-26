// SmartStay Hotel Management System
// Main.java - Console-based user interface driver

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("==================================================");
        System.out.println("      Welcome to SmartStay Hotel Management       ");
        System.out.println("==================================================");

      // Inside main() method on startup:
      List<Booking> loadedBookings = FileHandler.loadBookings();
      // Add or set loadedBookings into your hotel instance
      
        while (running) {
            System.out.println("\n----------------- MAIN MENU -----------------");
            System.out.println("1. View All Rooms");
            System.out.println("2. View Available Rooms");
            System.out.println("3. Book a Room");
            System.out.println("4. View All Bookings");
            System.out.println("5. View Specific Booking & Bill");
            System.out.println("6. Check Out Guest & Release Room");
            System.out.println("7. Exit");
            System.out.print("Enter your choice (1-7): ");

            String input = scanner.nextLine().trim();

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("\n[Error] Invalid input. Please enter a number between 1 and 7.");
                continue;
            }

            switch (choice) {
                case 1:
                    // 1. View all hotel rooms
                    hotel.displayAllRooms();
                    break;

                case 2:
                    // 2. View available rooms
                    hotel.displayAvailableRooms();
                    break;

                case 3:
                    // 3. Book a room for a guest
                    System.out.println("\n--- New Room Booking ---");
                    
                    System.out.print("Enter Guest ID (e.g., G101): ");
                    String guestId = scanner.nextLine().trim();
                    if (guestId.isEmpty()) {
                        System.out.println("[Error] Guest ID cannot be empty.");
                        break;
                    }

                    System.out.print("Enter Guest Name: ");
                    String guestName = scanner.nextLine().trim();
                    if (guestName.isEmpty()) {
                        System.out.println("[Error] Guest Name cannot be empty.");
                        break;
                    }

                    System.out.print("Enter Contact Number: ");
                    String contactNumber = scanner.nextLine().trim();
                    if (contactNumber.isEmpty()) {
                        System.out.println("[Error] Contact Number cannot be empty.");
                        break;
                    }

                    // Show available rooms to help receptionist choose
                    hotel.displayAvailableRooms();

                    System.out.print("Enter Room Number to book: ");
                    int roomNumber;
                    try {
                        roomNumber = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("[Error] Room number must be an integer.");
                        break;
                    }

                    System.out.print("Enter Number of Nights: ");
                    int nights;
                    try {
                        nights = Integer.parseInt(scanner.nextLine().trim());
                        if (nights <= 0) {
                            System.out.println("[Error] Number of nights must be greater than zero.");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[Error] Nights must be a positive integer.");
                        break;
                    }

                    Guest guest = new Guest(guestId, guestName, contactNumber);
                    hotel.createBooking(guest, roomNumber, nights);
                    break;

                case 4:
                    // 4. View all booking details
                    hotel.displayAllBookings();
                    break;

                case 5:
                    // 5. View specific booking details and bill
                    System.out.print("\nEnter Booking ID to search: ");
                    try {
                        int searchId = Integer.parseInt(scanner.nextLine().trim());
                        Booking foundBooking = hotel.findBookingById(searchId);
                        if (foundBooking != null) {
                            System.out.println("\nBooking Record Found:");
                            foundBooking.displayBookingDetails();
                        } else {
                            System.out.println("\n[Error] No booking found with ID #" + searchId);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("[Error] Booking ID must be a valid number.");
                    }
                    break;

                case 6:
                    // 6. Check out a guest and release the room
                    System.out.print("\nEnter Booking ID for Check-Out: ");
                    try {
                        int checkoutId = Integer.parseInt(scanner.nextLine().trim());
                        hotel.checkOutGuest(checkoutId);
                    } catch (NumberFormatException e) {
                        System.out.println("[Error] Booking ID must be a valid number.");
                    }
                    break;

                case 7:
                    // 7. Exit
                    System.out.println("\nThank you for using SmartStay Hotel Management System. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("\n[Error] Choice out of range. Please choose a valid option (1-7).");
                    break;
            }
        }

        scanner.close();
    }
}
