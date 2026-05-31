package exceptionPackage.appointment;

public class ReadAppointmentException extends Exception {
    private String wrongValue;

    public ReadAppointmentException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}