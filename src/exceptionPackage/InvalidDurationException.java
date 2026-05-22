package exceptionPackage;

public class InvalidDurationException extends Exception {
    private int wrongDuration;
    public InvalidDurationException(int wrongDuration, String message) {
        super(message);
        this.wrongDuration = wrongDuration;
    }

    public int getWrongDuration() {
        return wrongDuration;
    }
}