package exceptionPackage.appointment;

public class AppointmentBusinessException extends Exception {
    private String wrongValue;

    public AppointmentBusinessException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}