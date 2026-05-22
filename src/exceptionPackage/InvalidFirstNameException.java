package exceptionPackage;

public class InvalidFirstNameException extends Exception {
    private String wrongValue;
    public InvalidFirstNameException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
  }
}