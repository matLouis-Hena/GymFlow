package exceptionPackage.qualification;

public class ReadQualificationException extends Exception {
    private String wrongValue;

    public ReadQualificationException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}