package exceptionPackage.sponsorship;

public class ReadSponsorshipException extends Exception {
    private String wrongValue;

    public ReadSponsorshipException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}