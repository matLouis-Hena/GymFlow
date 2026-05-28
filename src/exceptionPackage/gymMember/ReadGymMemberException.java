package exceptionPackage.gymMember;

public class ReadGymMemberException extends Exception {
    private int wrongValue;

    public ReadGymMemberException(int wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public int getWrongValue() {
        return wrongValue;
    }
}