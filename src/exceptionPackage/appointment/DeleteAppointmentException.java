package exceptionPackage.appointment;

public class DeleteAppointmentException extends Exception {
    private String wrongValue;

    public DeleteAppointmentException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}