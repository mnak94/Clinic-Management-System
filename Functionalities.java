import java.util.*;

public class Functionalities {
    public List<Patient> patients = new ArrayList<>();
    public List<Physiotherapist> therapists = new ArrayList<>();
    public List<Booking> bookings = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public void addPatient() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter address: ");
        String addr = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        patients.add(new Patient(name, addr, phone));
        System.out.println("Patient added.");
    }

    public void removePatient() {
        System.out.print("Enter patient name to remove: ");
        String name = scanner.nextLine();
    
        Patient patientToRemove = patients.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    
        if (patientToRemove != null) {
            patients.remove(patientToRemove);
            System.out.println("Patient removed: " + name);
    
            // Remove any bookings associated with the removed patient
            bookings.removeIf(booking -> booking.getPatient().equals(patientToRemove));
            System.out.println("Associated bookings removed.");
    
        } else {
            System.out.println("Patient not found.");
        }
    }
   

}
