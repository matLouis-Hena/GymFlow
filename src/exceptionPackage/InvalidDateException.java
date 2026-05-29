package exceptionPackage;

import java.time.LocalDate;

public class InvalidDateException extends Exception {
    private LocalDate wrongValue;

    public InvalidDateException(LocalDate wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public LocalDate getWrongValue() { return wrongValue; }
}
