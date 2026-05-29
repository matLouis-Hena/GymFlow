package exceptionPackage.validation;

public class InvalidEmailException extends Exception {
    private String wrongValue;

    public InvalidEmailException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}
