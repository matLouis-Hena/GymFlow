package exceptionPackage.gymMember;

public class AddGymMemberException extends Exception {
    private String wrongValue;

    public AddGymMemberException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}