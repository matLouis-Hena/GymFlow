package exceptionPackage.validation;

public class InvalidDescriptionException extends Exception {
    private String wrongValue;

    public InvalidDescriptionException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}