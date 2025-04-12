import java.util.Arrays;
import java.util.List;

public class Physiotherapist {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String[] specialties;
    private List<Treatment> treatmentTimetable;
    private static int idCounter = 1; //static counter for auto-increment

    public Physiotherapist(String name, String address, String phone, String[] specialties, List<Treatment> treatmentTimetable) {
        this.id = idCounter++; 
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.specialties = specialties;
        this.treatmentTimetable = treatmentTimetable;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String[] getSpecialties() {
        return specialties;
    }

    public List<Treatment> getTreatmentTimetable() {
        return treatmentTimetable;
    }
    public List<String> getExpertiseAreas() {
        return Arrays.asList(specialties);
    }

    public void setTreatmentTimetable(List<Treatment> treatmentTimetable) {
        this.treatmentTimetable = treatmentTimetable;
    }

    public List<Treatment> getTreatments() {
        return treatmentTimetable; 
    }

    public String getSpecialtiesAsString() {
        return Arrays.toString(specialties);
    }

    @Override
    public String toString() {
        return "Physiotherapist ID: " + id + "\nName: " + name + "\nAddress: " + address +
               "\nPhone: " + phone + "\nSpecialties: " + getSpecialtiesAsString();
    }
}
