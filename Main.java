import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Functionalities f = new Functionalities();
        f.seedData();

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("""
                \n=== Boost Physio Clinic ===
                1. Add Patient
                2. Remove Patient
                3. Book Appointment
                4. Cancel Appointment
                5. Change Appointment
                6. Attend Appointment
                7. List Patients
                8. List Physiotherapists
                9. List Bookings
                10. Print Report
                0. Exit
                """);

            System.out.print("Enter your choice: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> f.addPatient();
                case 2 -> f.removePatient();
                case 3 -> f.bookAppointment();
                case 4 -> f.cancelAppointment();
                case 5 -> f.changeAppointment();
                case 6 -> f.attendAppointment();
                case 7 -> f.listPatients();
                case 8 -> f.listPhysiotherapists();
                case 9 -> f.listBookings();
                case 10 -> f.printReport();
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 0);
    }
}
