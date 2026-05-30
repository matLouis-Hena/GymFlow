package modelPackage;

public enum SubscriptionType {
    BASIC(
            "BASIC",
            "Basic",
            15.99,
            false,
            false,
            "salle de sport normale"
    ),
    STANDARD(
            "STANDARD",
            "Standard",
            24.99,
            true,
            false,
            "salle de sport normale + réservation coach"
    ),
    PREMIUM(
            "PREMIUM",
            "Premium",
            39.99,
            true,
            true,
            "salle de sport normale + réservation coach + facilities"
    );

    private final String databaseValue;
    private final String displayValue;
    private final double monthlyPrice;
    private final boolean canBookCoach;
    private final boolean hasFacilityAccess;
    private final String accessDescription;

    SubscriptionType(
            String databaseValue,
            String displayValue,
            double monthlyPrice,
            boolean canBookCoach,
            boolean hasFacilityAccess,
            String accessDescription
    ) {
        this.databaseValue = databaseValue;
        this.displayValue = displayValue;
        this.monthlyPrice = monthlyPrice;
        this.canBookCoach = canBookCoach;
        this.hasFacilityAccess = hasFacilityAccess;
        this.accessDescription = accessDescription;
    }

    public String getDatabaseValue() {
        return databaseValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public double getMonthlyPrice() {
        return monthlyPrice;
    }

    public boolean canBookCoach() {
        return canBookCoach;
    }

    public boolean hasFacilityAccess() {
        return hasFacilityAccess;
    }

    public String getAccessDescription() {
        return accessDescription;
    }

    public double calculatePrice(int durationMonths) {
        return monthlyPrice * durationMonths;
    }

    public static SubscriptionType fromDatabaseValue(String databaseValue) {
        for (SubscriptionType type : values()) {
            if (type.databaseValue.equalsIgnoreCase(databaseValue)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Type d'abonnement invalide : " + databaseValue);
    }

    @Override
    public String toString() {
        return displayValue + " - " + monthlyPrice + " €/mois";
    }
}