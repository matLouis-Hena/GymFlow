package exceptionPackage.gymMember;

public class UpdateGymMemberException extends Exception {
    private int wrongValue;

    public UpdateGymMemberException(int wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public int getWrongValue() {
        return wrongValue;
    }
}