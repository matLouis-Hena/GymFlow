package exceptionPackage;

public class InvalidCapacityException extends Exception {
    private int wrongValue;

    public InvalidCapacityException(int wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public int getWrongValue() {
        return wrongValue;
    }
}