

public class Guest {
   
    private String guestId;
    private String name;
    private String contactNumber;

  
    public Guest(String guestId, String name, String contactNumber) {
        this.guestId = guestId;
        this.name = name;
        this.contactNumber = contactNumber;
    }

  
    public String getGuestId() {
        return guestId;
    }

    public String getName() {
        return name;
    }

    public String getContactNumber() {
        return contactNumber;
    }


    public void displayGuestDetails() {
        System.out.println("Guest ID      : " + guestId);
        System.out.println("Guest Name    : " + name);
        System.out.println("Contact Number: " + contactNumber);
    }
}
