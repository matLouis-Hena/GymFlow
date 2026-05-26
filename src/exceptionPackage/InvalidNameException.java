package exceptionPackage;

public class InvalidNameException extends Exception {
    private String wrongValue;

    public InvalidNameException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}
