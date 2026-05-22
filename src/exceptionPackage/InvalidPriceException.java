package exceptionPackage;

public class InvalidPriceException extends Exception {
    private double wrongPrice;
    public InvalidPriceException(double wrongPrice, String message) {
        super(message);
        this.wrongPrice = wrongPrice;
    }

    public double getWrongPrice() {
        return wrongPrice;
    }
}