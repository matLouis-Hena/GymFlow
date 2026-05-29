package exceptionPackage.gymMember;

public class DuplicateGymMemberException extends Exception {
    private String wrongValue;

    public DuplicateGymMemberException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}