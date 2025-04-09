import java.util.Arrays;
import java.util.List;

public class Physiotherapist {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String[] specialties;
    private List<Treatment> treatmentTimetable;
    private static int idCounter = 1; 
    
    public Physiotherapist(String name, String address, String phone, String[] specialties, List<Treatment> treatmentTimetable) {
        this.id = idCounter++; 
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.specialties = specialties;
        this.treatmentTimetable = treatmentTimetable;
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
