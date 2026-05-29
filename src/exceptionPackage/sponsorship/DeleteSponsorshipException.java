package exceptionPackage.sponsorship;

public class DeleteSponsorshipException extends Exception {
    private String wrongValue;

    public DeleteSponsorshipException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}