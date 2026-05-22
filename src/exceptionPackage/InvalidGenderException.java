package exceptionPackage;

public class InvalidGenderException extends Exception {
    private char wrongGender;
    public InvalidGenderException(char wrongGender, String message) {
        super(message);
        this.wrongGender = wrongGender;
    }

    public char getWrongGender() {
        return wrongGender;
    }
}
