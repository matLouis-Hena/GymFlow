package exceptionPackage.gymMember;

public class DeleteGymMemberException extends Exception {
    private String wrongValue;

    public DeleteGymMemberException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}