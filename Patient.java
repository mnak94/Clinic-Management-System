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
}
