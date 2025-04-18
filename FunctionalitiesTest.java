import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.ArrayList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.io.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

// @ExtendWith(MockitoExtension.class) 
public class FunctionalitiesTest {

    private Functionalities functionalities;
    private Scanner mockScanner;

    @BeforeEach
    public void setUp() {
        functionalities = new Functionalities();
    }

    @Test
    public void testAddPatient() {
        String simulatedInput = "John Doe\n123 Main St\n1234567890\n";
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        functionalities.setScanner(new Scanner(System.in)); 
        functionalities.addPatient();

        assertEquals(1, functionalities.patients.size());
        Patient patient = functionalities.patients.get(0);
        assertEquals("John Doe", patient.getName());
        assertEquals("123 Main St", patient.getAddress());
        assertEquals("1234567890", patient.getPhone());
    }

    @Test
    public void testRemovePatient() {
        Patient p = new Patient("Alice", "123 Street", "9999999999");
        functionalities.patients.add(p);
        String input = "Alice\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        functionalities.setScanner(new Scanner(System.in));
        functionalities.removePatient();
        assertTrue(functionalities.patients.isEmpty());
    }

    @Test
    public void testListPatients() {
        functionalities.patients.add(new Patient("Tom", "City", "1111"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        functionalities.listPatients();
        String output = out.toString();
        assertTrue(output.contains("Listing all patients"));
        assertTrue(output.contains("Tom"));
    }

    @Test
    public void testListPhysiotherapists() {
        List<Treatment> treatments = new ArrayList<>();
        String[] specialties = {"Rehab", "Ortho"};
        Physiotherapist therapist = new Physiotherapist("Dr. Smith", "Clinic Road", "1234567890", specialties, treatments);
        functionalities.therapists.add(therapist);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        functionalities.listPhysiotherapists();
        String output = out.toString();
        assertTrue(output.contains("Listing all physiotherapists"));
        assertTrue(output.contains("Dr. Smith"));
    }

    @Test
    public void testListBookings() {
        Patient patient = new Patient("Lisa", "Park Lane", "2222");    
        String[] specialties = {"General"};
        List<Treatment> treatments = new ArrayList<>();
        Physiotherapist therapist = new Physiotherapist("Therapist A", "Wellness Ave", "0987654321", specialties, treatments);
        Treatment treatment = new Treatment("Rehab", "Monday 10AM", therapist);
        treatments.add(treatment);    
        Booking booking = new Booking(patient, therapist, treatment);
        functionalities.bookings.add(booking);    
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        functionalities.listBookings();
        String output = out.toString();
        assertTrue(output.contains("Listing all bookings"));
        assertTrue(output.contains("Lisa"));
    }

    @Test
    public void testCancelAppointment() {
        Patient patient = new Patient("John Doe", "123 Main St", "1234567890");
        String[] specialties = {"Rehab"};
        List<Treatment> treatments = new ArrayList<>();
        Physiotherapist therapist = new Physiotherapist("Dr. Smith", "Clinic Road", "1234567890", specialties, treatments);
        Treatment treatment = new Treatment("Rehab", "Monday 10AM", therapist);
        treatments.add(treatment);
        Booking booking = new Booking(patient, therapist, treatment);
        functionalities.bookings.add(booking);
        String simulatedInput = booking.getBookingId() + "\n";
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);
        functionalities.setScanner(new Scanner(System.in));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        functionalities.cancelAppointment();
        assertTrue(functionalities.bookings.isEmpty(), "Booking should be cancelled and removed.");
        assertTrue(out.toString().contains("Booking cancelled successfully."), "Expected cancellation success message.");
    }
 
    @Test
    public void testChangeAppointment() {
        List<Treatment> helenTimetable = new ArrayList<>();
        Physiotherapist helen = new Physiotherapist("Helen", "123 Clinic St", "555-1234",
                new String[]{"Physiotherapy", "Rehabilitation"}, helenTimetable);
        functionalities.therapists.add(helen);
        Treatment t1 = new Treatment("Joint Mobilization", "Week 1 - Monday 9-10 AM", helen);
        t1.book(); 
        helenTimetable.add(t1);
        Treatment t2 = new Treatment("Joint Mobilization", "Week 1 - Wednesday 11-12 AM", helen);
        helenTimetable.add(t2);
        Patient patient = new Patient("John Doe", "456 Patient St", "555-5678");
        functionalities.patients.add(patient);
        Booking booking = new Booking(patient, helen, t1);
        functionalities.bookings.add(booking);
        String simulatedInput = booking.getBookingId() + "\n" +
                                "Joint Mobilization\n" +
                                "1\n";
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);
        functionalities.setScanner(new Scanner(System.in));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        functionalities.changeAppointment();
        String output = out.toString();
        assertTrue(output.contains("Booking changed successfully."), "Booking should be changed");
        assertEquals(t2, booking.getTreatment(), "Booking should point to new treatment");
        assertTrue(t1.isBooked() == false, "Old treatment should be unbooked");
        assertTrue(t2.isBooked(), "New treatment should be booked");
    }

    @Test
    public void testAttendAppointment() {
        String simulatedInput = "John Doe\n";
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in); 
        Functionalities functionalities = new Functionalities();
        List<Treatment> helenTimetable = new ArrayList<>();
        Physiotherapist helen = new Physiotherapist("Helen", "123 Clinic St", "555-1234",
                new String[]{"Physiotherapy", "Rehabilitation"}, helenTimetable);
        functionalities.therapists.add(helen);
        Treatment t1 = new Treatment("Joint Mobilization", "Week 1 - Monday 9-10 AM", helen);
        helenTimetable.add(t1);
        Patient patient = new Patient("John Doe", "456 Patient St", "555-5678");
        functionalities.patients.add(patient);
        t1.book();
        Booking booking = new Booking(patient, helen, t1);
        functionalities.bookings.add(booking);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        functionalities.attendAppointment();
        String output = out.toString();
        assertTrue(output.contains("Appointment marked as attended."));
        assertTrue(booking.isAttended());
    }

    @Test
    public void testPrintReport() {
        Functionalities functionalities = new Functionalities();
        List<Treatment> helenTimetable = new ArrayList<>();
        Physiotherapist helen = new Physiotherapist("Helen", "123 Clinic St", "555-1234",
                new String[]{"Physiotherapy", "Rehabilitation"}, helenTimetable);
        functionalities.therapists.add(helen);
        Treatment t1 = new Treatment("Joint Mobilization", "Week 1 - Monday 9-10 AM", helen);
        helenTimetable.add(t1);
        Patient patient = new Patient("John Doe", "456 Patient St", "555-5678");
        functionalities.patients.add(patient);
        t1.book();
        Booking booking = new Booking(patient, helen, t1);
        functionalities.bookings.add(booking);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        functionalities.printReport();
        String output = outContent.toString();
        assertTrue(output.contains("Weekly Appointment Report"));
        assertTrue(output.contains("Physiotherapist: Helen"));
        assertTrue(output.contains("Week 1:"));
        assertTrue(output.contains("Joint Mobilization"));
        assertTrue(output.contains("Monday 9-10 AM"));
        assertTrue(output.contains("[BOOKED]"));
        assertTrue(output.contains("Patient: John Doe"));
    }

}

// javac -cp "lib/*" -d out/ *.java
// java -cp "lib/*;out" org.junit.platform.console.ConsoleLauncher --scan-classpath
