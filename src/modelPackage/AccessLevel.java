package modelPackage;

public enum AccessLevel {
    ADMIN("Administrateur", "Accès complet à toutes les fonctionnalités"),
    MANAGER("Manager", "Accès aux gestions des adhérents, coachs et salles"),
    MODERATOR("Modérateur", "Accès limité aux consultations et modérations");

    private final String displayName;
    private final String description;

    AccessLevel(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public static AccessLevel fromString(String name) {
        try {
            return AccessLevel.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Niveau d'accès invalide: " + name);
        }
    }
}