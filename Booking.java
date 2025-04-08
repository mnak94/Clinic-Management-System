public class Booking {
    private static int idCounter = 1;

    private String bookingId;
    private Patient patient;
    private Physiotherapist therapist;
    private Treatment treatment;
    private boolean attended;

    public Booking(Patient patient, Physiotherapist therapist, Treatment treatment) {
        this.bookingId = "B" + idCounter++;
        this.patient = patient;
        this.therapist = therapist;
        this.treatment = treatment;
        this.attended = false; // Default to false
    }
}
