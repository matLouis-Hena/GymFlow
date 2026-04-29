package exceptionPackage;

public class InvalidLockerNumberException extends Exception {
    private int wrongValue;
    public InvalidLockerNumberException(int wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }
    public int getWrongValue() {
        return wrongValue;
    }
}
