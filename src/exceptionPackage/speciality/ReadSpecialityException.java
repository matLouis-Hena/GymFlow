package exceptionPackage.speciality;

public class ReadSpecialityException extends Exception {
    private String wrongValue;

    public ReadSpecialityException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}