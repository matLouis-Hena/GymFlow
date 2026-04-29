package modelPackage;

public enum AppointmentStatus {
    PENDING("En attente", "Le rendez-vous est en attente de confirmation"),
    CONFIRMED("Confirmé", "Le rendez-vous est confirmé"),
    DONE("Finit", "Le rendez-vous a été effectué"),
    CANCELLED("Annulé", "Le rendez-vous a été annulé");

    private final String displayName;
    private final String description;

    AppointmentStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public static AppointmentStatus fromString(String name) {
        try {
            return AppointmentStatus.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Statut d'appointment invalide: " + name);
        }
    }
}