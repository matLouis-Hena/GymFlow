package exceptionPackage.coach;

public class ReadCoachException extends Exception {
    private String wrongValue;

    public ReadCoachException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}