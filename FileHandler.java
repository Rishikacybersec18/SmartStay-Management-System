import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Enterprise Utility class handling persistent flat-file storage for the SmartStay System.
 * Encapsulates thread-safe stream reading and writing operations for reservation records.
 *
 * @author Member 2 - Lead Systems Architect
 */
public class FileHandler {

    private static String filePath = "bookings.txt";
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Configures a non-default file storage path (primarily used for test isolation).
     *
     * @param path The relative or absolute file path to target.
     */
    public static void setFilePath(String path) {
        lock.writeLock().lock();
        try {
            filePath = path;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Gets the currently active storage file path.
     *
     * @return Current file path string.
     */
    public static String getFilePath() {
        lock.readLock().lock();
        try {
            return filePath;
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Appends a single reservation record to persistent disk storage.
     * Thread-safe operation enforced via Write Lock.
     *
     * @param booking The Booking object instance to persist.
     */
    public static void saveBooking(Booking booking) {
        if (booking == null) {
            System.err.println("[FileHandler Error] Aborting persist operation: Null booking record.");
            return;
        }

        lock.writeLock().lock();
        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println(booking.toCsv());
            System.out.println("[FileHandler] Successfully persisted record: " + booking.getBookingId());

        } catch (IOException e) {
            System.err.println("[FileHandler Exception] Standard File I/O Failure while saving record: " + e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Overwrites persistent storage with a complete collection of reservation records.
     * Thread-safe operation enforced via Write Lock.
     *
     * @param bookings Collection of Booking instances to write.
     */
    public static void saveAllBookings(List<Booking> bookings) {
        if (bookings == null) {
            System.err.println("[FileHandler Error] Aborting bulk persist operation: Null collection.");
            return;
        }

        lock.writeLock().lock();
        try (FileWriter fw = new FileWriter(filePath, false);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            for (Booking booking : bookings) {
                if (booking != null) {
                    out.println(booking.toCsv());
                }
            }
            System.out.println("[FileHandler] Bulk persistence operation completed. Records saved: " + bookings.size());

        } catch (IOException e) {
            System.err.println("[FileHandler Exception] Standard File I/O Failure during bulk save: " + e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Reads, parses, and rehydrates stored records into domain objects.
     * Thread-safe operation enforced via Read Lock.
     *
     * @return List of rehydrated Booking objects. Returns an empty collection if file is unreadable or absent.
     */
    public static List<Booking> loadBookings() {
        List<Booking> loadedBookings = new ArrayList<>();

        lock.readLock().lock();
        try {
            File file = new File(filePath);

            if (!file.exists()) {
                System.out.println("[FileHandler] Repository file '" + filePath + "' not found. Initialising empty state.");
                return loadedBookings;
            }

            try (FileReader fr = new FileReader(file);
                 BufferedReader br = new BufferedReader(fr)) {

                String line;
                int lineNumber = 0;
                while ((line = br.readLine()) != null) {
                    lineNumber++;
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue;
                    }

                    try {
                        Booking booking = Booking.fromCsv(line);
                        if (booking != null) {
                            loadedBookings.add(booking);
                        }
                    } catch (NumberFormatException nfe) {
                        System.err.println("[FileHandler Warning] Skipping row " + lineNumber 
                                           + ": Malformed numeric token. Details: " + nfe.getMessage());
                    } catch (IllegalArgumentException iae) {
                        System.err.println("[FileHandler Warning] Skipping row " + lineNumber 
                                           + ": Token structure invalid. Details: " + iae.getMessage());
                    }
                }
                System.out.println("[FileHandler] Successfully rehydrated " + loadedBookings.size() + " records from storage.");

            } catch (IOException e) {
                System.err.println("[FileHandler Exception] Critical failure accessing repository stream: " + e.getMessage());
            }
        } finally {
            lock.readLock().unlock();
        }

        return loadedBookings;
    }
}
