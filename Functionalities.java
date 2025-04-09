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

}
