package exceptionPackage.gymMember;

public class ReadGymMemberException extends Exception {
    private String wrongValue;

    public ReadGymMemberException(String wrongValue, String message) {
        super(message);
        this.wrongValue = wrongValue;
    }

    public String getWrongValue() {
        return wrongValue;
    }
}