package modelPackage;

public enum SubscriptionType {
    BASIC("BASIC", "Basic", 29.99),
    STANDARD("STANDARD", "Standard", 24.99),
    PREMIUM("PREMIUM", "Premium", 19.99);

    private final String databaseValue;
    private final String displayValue;
    private final double monthlyPrice;

    SubscriptionType(String databaseValue, String displayValue, double monthlyPrice) {
        this.databaseValue = databaseValue;
        this.displayValue = displayValue;
        this.monthlyPrice = monthlyPrice;
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

    public double calculatePrice(int durationMonths) {
        return Math.round(monthlyPrice * durationMonths * 100.0) / 100.0;
    }

    public static SubscriptionType fromDatabaseValue(String databaseValue) {
        for (SubscriptionType type : values()) {
            if (type.databaseValue.equals(databaseValue)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown subscription type: " + databaseValue);
    }

    @Override
    public String toString() {
        return displayValue + " - " + monthlyPrice + "€/mois";
    }
}