package exceptionPackage.validation;

public class InvalidPhoneException extends Exception {
    private String wrongValue;

    public InvalidPhoneException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}