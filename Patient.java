public class Patient {
    private static int idCounter = 1;
    private final int id;
    private String name;
    private String address;
    private String phone;


    public Patient(String name, String address, String phone) {
        this.id = idCounter++;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }
    public int getId() { return id; }
    public String getAddress() { return address; }
    public String getName() { return name; }
    public String getPhone() { return phone; }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Patient.idCounter = idCounter;
    }
    
    @Override
    public String toString() {
        return "Patient #" + id + ": " + name + ", " + address + ", " + phone;
    }
}
