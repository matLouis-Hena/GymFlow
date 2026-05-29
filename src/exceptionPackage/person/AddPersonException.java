package exceptionPackage.person;

public class AddPersonException extends Exception {
    private String wrongValue;

    public AddPersonException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}