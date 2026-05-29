package exceptionPackage.sponsorship;

public class AddSponsorshipException extends Exception {
    private String wrongValue;

    public AddSponsorshipException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}
