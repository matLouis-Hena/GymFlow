package exceptionPackage;

public class InvalidLastNameException extends Exception {
    private String wrongValue;
    public InvalidLastNameException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}
