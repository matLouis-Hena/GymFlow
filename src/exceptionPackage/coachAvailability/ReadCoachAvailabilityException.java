package exceptionPackage.coachAvailability;

public class ReadCoachAvailabilityException extends Exception {
    private String wrongValue;

    public ReadCoachAvailabilityException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}