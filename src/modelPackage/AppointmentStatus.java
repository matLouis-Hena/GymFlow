package modelPackage;

public enum AppointmentStatus {
    PENDING("En attente", "Le rendez-vous est en attente de confirmation"),
    CONFIRMED("Confirmé", "Le rendez-vous est confirmé"),
    DONE("Fini", "Le rendez-vous a été effectué"),
    CANCELLED_BY_COACH("Annulé", "Le rendez-vous a été annulé par le coach"),
    CANCELLED_BY_MEMBER("Annulé", "Le rendez-vous a été annulé par le membre");


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
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Statut d'appointment invalide: " + name);
        }
    }
}