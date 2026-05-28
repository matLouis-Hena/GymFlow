package exceptionPackage.gymMember;

public class DeleteGymMemberException extends Exception {
    private int wrongValue;

    public DeleteGymMemberException(int wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public int getWrongValue() {
        return wrongValue;
    }
}