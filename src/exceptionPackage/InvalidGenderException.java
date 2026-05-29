package exceptionPackage;

import modelPackage.Gender;

public class InvalidGenderException extends Exception {
    private Gender wrongGender;
    public InvalidGenderException(Gender wrongGender, String message) {
        super(message);
        this.wrongGender = wrongGender;
    }

    public Gender getWrongGender() {
        return wrongGender;
    }
}
