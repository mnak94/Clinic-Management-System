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
        this.attended = false; 
    }

    public String getBookingId() { return bookingId; }
    public Patient getPatient() { return patient; }
    public Physiotherapist getTherapist() { return therapist; }
    public void setTherapist(Physiotherapist therapist) { 
        this.therapist = therapist; 
    }
    
    public Treatment getTreatment() {
        return treatment;
    }
    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }
    public void attend() { attended = true; }
    public boolean isAttended() { return attended; }

    @Override
    public String toString() {
        return "Booking " + bookingId + " for " + patient.getName() + " with " + therapist.getName() +
               " for " + treatment.getName() + " at " + treatment.getDateTime() +
               " | Attended: " + (attended ? "Yes" : "No");
    }
}
