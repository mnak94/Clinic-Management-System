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
            bookings.removeIf(booking -> booking.getPatient().equals(patientToRemove));
            System.out.println("Associated bookings removed.");
    
        } else {
            System.out.println("Patient not found.");
        }
    }
    
    public void listPatients() {
        System.out.println("Listing all patients:");
        for (Patient p : patients) {
            System.out.println(p);
        }
    }

    public void listPhysiotherapists() {
        System.out.println("Listing all physiotherapists:");
        for (Physiotherapist p : therapists) {
            System.out.println(p);
        }
    }

    public void listBookings() {
        System.out.println("Listing all bookings:");
        for (Booking b : bookings) {
            System.out.println(b);
        }
    }

    public List<Booking> getBookings() {
        return bookings;
    }
    
    public void bookAppointment() {
        System.out.println("1. Book by Expertise\n2. Book by Therapist Name");
        int choice = Integer.parseInt(scanner.nextLine());

        List<Physiotherapist> availableTherapists = new ArrayList<>();

        if (choice == 1) {
            Set<String> uniqueExpertise = therapists.stream()
                    .flatMap(t -> t.getExpertiseAreas().stream())
                    .collect(Collectors.toSet());

            int index = 1;
            for (String expertise : uniqueExpertise) {
                System.out.println(index++ + ". " + expertise);
            }

            System.out.print("Enter expertise: ");
            String expertise = scanner.nextLine();

            availableTherapists = therapists.stream()
                    .filter(t -> t.getExpertiseAreas().contains(expertise))
                    .collect(Collectors.toList());

            if (availableTherapists.isEmpty()) {
                System.out.println("No therapists available with the chosen expertise.");
                return;
            }
        } else if (choice == 2) {
            availableTherapists = new ArrayList<>(therapists);
        }

        availableTherapists.forEach(t -> {
            System.out.println("\nTherapist: " + t.getName());
            System.out.println("Available Treatments:");
            t.getTreatments().forEach(tr -> {
                boolean isBooked = bookings.stream()
                        .anyMatch(b -> b.getTreatment().getDateTime().equals(tr.getDateTime()));
                System.out.println("   - " + tr.getName() + " (" + tr.getDateTime() + ")" + (isBooked ? " [Booked]" : ""));
            });
        });

        System.out.print("Enter therapist name: ");
        String therapistName = scanner.nextLine();
        Physiotherapist therapist = therapists.stream()
                .filter(t -> t.getName().equalsIgnoreCase(therapistName))
                .findFirst().orElse(null);

        System.out.print("\nEnter patient name: ");
        String patientName = scanner.nextLine();
        Patient patient = patients.stream()
                .filter(p -> p.getName().equalsIgnoreCase(patientName))
                .findFirst().orElse(null);

        System.out.print("Enter treatment name: ");
        String treatmentName = scanner.nextLine();
        Treatment treatment = therapist != null ? therapist.getTreatments().stream()
                .filter(tr -> tr.getName().equalsIgnoreCase(treatmentName) && !tr.isBooked())
                .findFirst().orElse(null) : null;

        if (treatment != null) {
            boolean isSlotAvailable = bookings.stream()
                    .noneMatch(b -> b.getTreatment().getDateTime().equals(treatment.getDateTime()));
            if (!isSlotAvailable) {
                System.out.println("Error: The selected time slot is already booked.");
                return;
            }
            if (patient != null && therapist != null) {
                treatment.book();
                bookings.add(new Booking(patient, therapist, treatment));
                System.out.println("Appointment booked!");
            } else {
                System.out.println("Error booking appointment. Please ensure all inputs are correct.");
            }
        } else {
            System.out.println("Error: Treatment not available or already booked.");
        }
    }

   

}
