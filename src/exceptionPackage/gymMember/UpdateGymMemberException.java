package exceptionPackage.gymMember;

public class UpdateGymMemberException extends Exception {
    private String wrongValue;

    public UpdateGymMemberException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}