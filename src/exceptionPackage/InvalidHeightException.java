package exceptionPackage;

public class InvalidHeightException extends Exception {
    private int wrongValue;
    public InvalidHeightException(int wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public int getWrongValue() {
        return wrongValue;
    }
}