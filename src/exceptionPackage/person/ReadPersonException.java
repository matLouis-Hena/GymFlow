package exceptionPackage.person;

public class ReadPersonException extends Exception {
    private String wrongValue;

    public ReadPersonException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}