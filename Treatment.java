
public class Treatment {
    private String name;
    private String dateTime;
    private boolean isBooked;
    private Physiotherapist physiotherapist;

    public Treatment(String name, String dateTime, Physiotherapist physiotherapist) {
        this.name = name;
        this.dateTime = dateTime;
        this.physiotherapist = physiotherapist;
    }
}
