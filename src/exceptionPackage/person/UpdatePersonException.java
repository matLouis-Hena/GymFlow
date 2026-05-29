package exceptionPackage.person;

public class UpdatePersonException extends Exception {
    private String wrongValue;

    public UpdatePersonException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}