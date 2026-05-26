package modelPackage;

public enum Gender {

    Male("Masculin"),
    Female("Féminin"),
    Other("Autre");

    private final String databaseValue;

    Gender(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    public String getDatabaseValue() {
        return databaseValue;
    }

    public static Gender fromDatabaseValue(String value) {

        for(Gender gender : values()) {

            if(gender.databaseValue.equalsIgnoreCase(value)) {
                return gender;
            }
        }

        throw new IllegalArgumentException(
                "Valeur de genre invalide : " + value
        );
    }
}