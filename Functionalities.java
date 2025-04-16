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

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void cancelAppointment() {
        System.out.print("Enter Booking ID to cancel: ");
        String bookingId = scanner.nextLine();
    
        Booking bookingToCancel = null;
    
        for (Booking booking : bookings) {
            if (booking.getBookingId().equalsIgnoreCase(bookingId)) {
                bookingToCancel = booking;
                break;
            }
        }
    
        if (bookingToCancel != null) {
            bookingToCancel.getTreatment().cancel(); // unbook treatment
            bookings.remove(bookingToCancel);        // remove booking
            System.out.println("Booking cancelled successfully.");
        } else {
            System.out.println("No booking found with that ID.");
        }
    }
    

    public void changeAppointment() {
        System.out.print("Enter Booking ID to change: ");
        String bookingId = scanner.nextLine();
    
        Booking bookingToChange = null;
        for (Booking booking : bookings) {
            if (booking.getBookingId().equalsIgnoreCase(bookingId)) {
                bookingToChange = booking;
                break;
            }
        }
    
        if (bookingToChange == null) {
            System.out.println("No booking found with that ID.");
            return;
        }
    
        Set<String> treatmentNames = new HashSet<>();
        for (Physiotherapist therapist : therapists) {
            for (Treatment treatment : therapist.getTreatments()) {
                treatmentNames.add(treatment.getName());
            }
        }
    
        if (treatmentNames.isEmpty()) {
            System.out.println("No treatments available.");
            return;
        }
    
        System.out.println("Available Treatments:");
        int i = 1;
        List<String> treatmentList = new ArrayList<>(treatmentNames);
        for (String t : treatmentList) {
            System.out.println(i++ + ". " + t);
        }
    
        System.out.print("Enter the name of the new treatment: ");
        String selectedTreatmentName = scanner.nextLine();
    
        List<Physiotherapist> offeringTherapists = new ArrayList<>();
        for (Physiotherapist therapist : therapists) {
            boolean offers = therapist.getTreatments().stream()
                    .anyMatch(t -> t.getName().equalsIgnoreCase(selectedTreatmentName));
            if (offers) {
                offeringTherapists.add(therapist);
            }
        }
    
        if (offeringTherapists.isEmpty()) {
            System.out.println("No therapists offer that treatment.");
            return;
        }
    
        System.out.println("\nTherapists with available slots for '" + selectedTreatmentName + "':");
        Map<Integer, Treatment> availableOptions = new HashMap<>();
        int optionIndex = 1;
    
        for (Physiotherapist therapist : offeringTherapists) {
            for (Treatment treatment : therapist.getTreatments()) {
                if (treatment.getName().equalsIgnoreCase(selectedTreatmentName) && !treatment.isBooked()) {
                    System.out.println(optionIndex + ". " + treatment.getName() + " by " + therapist.getName() +
                            " at " + treatment.getDateTime());
                    availableOptions.put(optionIndex, treatment);
                    optionIndex++;
                }
            }
        }
    
        if (availableOptions.isEmpty()) {
            System.out.println("Sorry, all slots for this treatment are booked.");
            return;
        }
    
        System.out.print("Select an option number for the new appointment: ");
        int selectedOption = Integer.parseInt(scanner.nextLine());
    
        Treatment newTreatment = availableOptions.get(selectedOption);
    
        if (newTreatment == null) {
            System.out.println("Invalid selection.");
            return;
        }
    
        bookingToChange.getTreatment().cancel(); // cancel old
        newTreatment.book();                     // book new
        bookingToChange.setTreatment(newTreatment);
        bookingToChange.setTherapist(
                therapists.stream()
                          .filter(t -> t.getTreatments().contains(newTreatment))
                          .findFirst().orElse(bookingToChange.getTherapist())
        );
    
        System.out.println("Booking changed successfully.");
    }
    
    
    

    public void attendAppointment() {
        System.out.print("Enter patient name: ");
        String name = scanner.nextLine();
        Booking b = bookings.stream().filter(book -> book.getPatient().getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        if (b != null) {
            b.attend();
            System.out.println("Appointment marked as attended.");
        } else {
            System.out.println("No appointment found.");
        }
    }

    public void printReport() {
        System.out.println("===== Weekly Appointment Report =====\n");
    
        for (Physiotherapist physio : therapists) {
            System.out.println("Physiotherapist: " + physio.getName());
            System.out.print("Expertise: ");
            System.out.println(String.join(", ", physio.getExpertiseAreas()));
            System.out.println("----------------------------------------------------\n");
    
            Map<String, List<Treatment>> weeklySchedule = new LinkedHashMap<>();
    
            for (Treatment treatment : physio.getTreatmentTimetable()) {
                String[] parts = treatment.getDateTime().split(" - ", 2);
                String weekKey = parts[0].trim();  // "Week 1"
                weeklySchedule.putIfAbsent(weekKey, new ArrayList<>());
                weeklySchedule.get(weekKey).add(treatment);
            }
    
            for (String week : weeklySchedule.keySet()) {
                System.out.println(week + ":");
                for (Treatment treatment : weeklySchedule.get(week)) {
                    String[] parts = treatment.getDateTime().split(" - ", 2);
                    String timeSlot = parts.length > 1 ? parts[1].trim() : "Unknown";
                    String status = treatment.isBooked() ? "[BOOKED]" : "[AVAILABLE]";
                    String patientName = "";
    
                    if (treatment.isBooked()) {
                        for (Booking booking : bookings) {
                            if (booking.getTreatment().equals(treatment)) {
                                patientName = " - Patient: " + booking.getPatient().getName();
                                break;
                            }
                        }
                    }
    
                    System.out.printf("  * %-30s %-25s %s%s\n",
                            treatment.getName(),
                            timeSlot,
                            status,
                            patientName);
                }
                System.out.println();
            }
    
            System.out.println();
        }
    }
    

    public void seedData() {
        List<Treatment> helenTimetable = new ArrayList<>();
        Physiotherapist helen = new Physiotherapist("Helen", "123 Clinic St", "555-1234",
                new String[]{"Physiotherapy", "Rehabilitation"}, helenTimetable);
    
        List<Treatment> davidTimetable = new ArrayList<>();
        Physiotherapist david = new Physiotherapist("David", "456 Health Ave", "555-2345",
                new String[]{"Sports Medicine", "Orthopaedics"}, davidTimetable);
    
        List<Treatment> sophiaTimetable = new ArrayList<>();
        Physiotherapist sophia = new Physiotherapist("Sophia", "789 Wellness St", "555-3456",
                new String[]{"Sports Rehabilitation", "Physiotherapy", "Manual Therapy"}, sophiaTimetable);
    
        therapists.add(helen);
        therapists.add(david);
        therapists.add(sophia);
    
        helenTimetable.add(new Treatment("Joint Mobilization", "Week 1 - Monday 9-10 AM", helen));
        helenTimetable.add(new Treatment("Rehab Stretching", "Week 1 - Wednesday 2-3 PM", helen));
        helenTimetable.add(new Treatment("Post-Surgery Rehab", "Week 2 - Tuesday 11-12 AM", helen));
        helenTimetable.add(new Treatment("Myofascial Release", "Week 2 - Friday 3-4 PM", helen));
        helenTimetable.add(new Treatment("Rehab Circuit Training", "Week 3 - Monday 8-9 AM", helen));
        helenTimetable.add(new Treatment("Balance Training", "Week 3 - Thursday 10-11 AM", helen));
        helenTimetable.add(new Treatment("Neuromuscular Re-education", "Week 4 - Wednesday 1-2 PM", helen));
        helenTimetable.add(new Treatment("Postural Alignment", "Week 4 - Friday 9-10 AM", helen));
    
        davidTimetable.add(new Treatment("ACL Recovery Program", "Week 1 - Tuesday 10-11 AM", david));
        davidTimetable.add(new Treatment("Sports Injury Management", "Week 1 - Thursday 1-2 PM", david));
        davidTimetable.add(new Treatment("Shoulder Stabilization", "Week 2 - Monday 12-1 PM", david));
        davidTimetable.add(new Treatment("Orthopaedic Massage", "Week 2 - Wednesday 11-12 AM", david));
        davidTimetable.add(new Treatment("Rotator Cuff Protocol", "Week 3 - Tuesday 3-4 PM", david));
        davidTimetable.add(new Treatment("Sports Biomechanics Eval", "Week 3 - Friday 9-10 AM", david));
        davidTimetable.add(new Treatment("Joint Pain Relief", "Week 4 - Monday 2-3 PM", david));
        davidTimetable.add(new Treatment("Tendonitis Care Plan", "Week 4 - Thursday 4-5 PM", david));
    
        sophiaTimetable.add(new Treatment("Manual Therapy", "Week 1 - Monday 11-12 AM", sophia));
        sophiaTimetable.add(new Treatment("Sports Conditioning", "Week 1 - Wednesday 4-5 PM", sophia));
        sophiaTimetable.add(new Treatment("Trigger Point Release", "Week 2 - Thursday 9-10 AM", sophia));
        sophiaTimetable.add(new Treatment("Kinesio Taping", "Week 2 - Friday 11-12 AM", sophia));
        sophiaTimetable.add(new Treatment("Spinal Mobilization", "Week 3 - Tuesday 10-11 AM", sophia));
        sophiaTimetable.add(new Treatment("Advanced Physio Plan", "Week 3 - Thursday 2-3 PM", sophia));
        sophiaTimetable.add(new Treatment("Sports Posture Training", "Week 4 - Monday 1-2 PM", sophia));
        sophiaTimetable.add(new Treatment("Muscle Energy Technique", "Week 4 - Friday 3-4 PM", sophia));
    
        patients.add(new Patient("John Doe", "456 Patient St", "555-5678"));
        patients.add(new Patient("Jane Smith", "123 Baker St", "555-6789"));
        patients.add(new Patient("Emily Johnson", "789 Pine Rd", "555-7890"));
        patients.add(new Patient("Chris Lee", "321 Oak St", "555-8901"));
        patients.add(new Patient("Michael Brown", "654 Cedar Dr", "555-9012"));
        patients.add(new Patient("Sarah Wilson", "432 Maple Ave", "555-0123"));
        patients.add(new Patient("Daniel Scott", "987 Birch Ln", "555-1122"));
        patients.add(new Patient("Emma Davis", "341 Spruce Rd", "555-2233"));
        patients.add(new Patient("Liam Taylor", "215 Willow Dr", "555-3344"));
        patients.add(new Patient("Olivia Harris", "129 Elm St", "555-4455"));
        patients.add(new Patient("Noah Thomas", "778 Juniper Ct", "555-5566"));
        patients.add(new Patient("Ava Martin", "630 Redwood Blvd", "555-6677"));
    
        Treatment t1 = helenTimetable.get(0); t1.book(); bookings.add(new Booking(patients.get(0), helen, t1));
        Treatment t2 = davidTimetable.get(1); t2.book(); bookings.add(new Booking(patients.get(1), david, t2));
        Treatment t3 = sophiaTimetable.get(4); t3.book(); bookings.add(new Booking(patients.get(2), sophia, t3));
        Treatment t4 = helenTimetable.get(3); t4.book(); bookings.add(new Booking(patients.get(3), helen, t4));
        Treatment t5 = davidTimetable.get(5); t5.book(); bookings.add(new Booking(patients.get(4), david, t5));
        Treatment t6 = sophiaTimetable.get(0); t6.book(); bookings.add(new Booking(patients.get(5), sophia, t6));
    }
    
        
    
}
