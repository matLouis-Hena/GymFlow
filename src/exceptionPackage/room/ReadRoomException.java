package exceptionPackage.room;

public class ReadRoomException extends Exception {
    private String wrongValue;

    public ReadRoomException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}