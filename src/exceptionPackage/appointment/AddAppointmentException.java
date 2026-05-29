package exceptionPackage.appointment;

public class AddAppointmentException extends Exception {
    private String wrongValue;

    public AddAppointmentException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}