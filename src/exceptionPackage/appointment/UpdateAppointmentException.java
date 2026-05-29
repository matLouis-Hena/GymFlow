package exceptionPackage.appointment;

public class UpdateAppointmentException extends Exception {
    private String wrongValue;

    public UpdateAppointmentException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}