package modelPackage;

public enum AppointmentStatus {
    PENDING("En attente", "Le rendez-vous est en attente de confirmation"),
    CONFIRMED("Confirme", "Le rendez-vous est confirme"),
    DONE("Fini", "Le rendez-vous a ete effectue"),
    CANCELLED_BY_COACH("Annule par coach", "Le rendez-vous a ete annule par le coach"),
    CANCELLED_BY_MEMBER("Annule par membre", "Le rendez-vous a ete annule par le membre"),
    CANCELLED_BY_ADMIN("Annule par admin", "Le rendez-vous a ete annule par un administrateur");

    private final String displayName;
    private final String description;

    AppointmentStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public static AppointmentStatus fromString(String name) {
        try {
            return AppointmentStatus.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Statut d'appointment invalide: " + name);
        }
    }
}
