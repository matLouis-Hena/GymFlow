package modelPackage;

public class Speciality {
    private String name;
    private String description;

    public Speciality(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}