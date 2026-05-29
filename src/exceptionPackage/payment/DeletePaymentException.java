package exceptionPackage.payment;

public class DeletePaymentException extends Exception {
    private String wrongValue;

    public DeletePaymentException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}