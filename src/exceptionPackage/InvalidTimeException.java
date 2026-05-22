package exceptionPackage;

import java.sql.Time;

public class InvalidTimeException extends Exception {
    private Time wrongValue;
    public InvalidTimeException(Time wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public Time getWrongValue() {
        return wrongValue;
    }
}