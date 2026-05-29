package exceptionPackage.coachAvailability;

public class UpdateCoachAvailabilityException extends Exception {
    private String wrongValue;

    public UpdateCoachAvailabilityException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}