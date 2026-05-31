package exceptionPackage.subscription;

public class ReadSubscriptionException extends Exception {
    private String wrongValue;

    public ReadSubscriptionException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}