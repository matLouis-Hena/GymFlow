package exceptionPackage.payment;

public class ReadPaymentException extends Exception {
    private String wrongValue;

    public ReadPaymentException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}