package exceptionPackage.person;

public class DeletePersonException extends Exception {
    private String wrongValue;

    public DeletePersonException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}