package modelPackage;

public enum UserRole {
    ADMIN("Administrateur"),
    COACH("Coach"),
    MEMBER_WITH_SUBSCRIPTION("Membre avec abonnement"),
    MEMBER_WITHOUT_SUBSCRIPTION("Membre sans abonnement");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
