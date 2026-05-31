package exceptionPackage.validation;

public class InvalidUsernameException extends Exception {
    private String wrongValue;

    public InvalidUsernameException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}
