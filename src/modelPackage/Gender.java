package modelPackage;

public enum Gender {

    MALE("MALE", "Masculin"),
    FEMALE("FEMALE", "Féminin"),
    OTHER("OTHER", "Autre");

    private final String databaseValue;
    private final String displayValue;

    Gender(String databaseValue, String displayValue) {
        this.databaseValue = databaseValue;
        this.displayValue = displayValue;
    }

    public String getDatabaseValue() {
        return databaseValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public static Gender fromDatabaseValue(String value) {

        for (Gender gender : values()) {

            if (gender.databaseValue.equalsIgnoreCase(value)) {
                return gender;
            }
        }

        throw new IllegalArgumentException(
                "Valeur de genre invalide : " + value
        );
    }

    @Override
    public String toString() {
        return displayValue;
    }
}