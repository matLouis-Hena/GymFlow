package exceptionPackage.validation;

public class InvalidWeightException extends Exception {
    private double wrongValue;

    public InvalidWeightException(double wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public double getWrongValue() {
        return wrongValue;
    }
}