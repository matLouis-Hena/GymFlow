package modelPackage;

import exceptionPackage.validation.InvalidDescriptionException;
import exceptionPackage.validation.InvalidNameException;

public class Speciality {
    private String name;
    private String description;

    public Speciality(String name, String description) throws InvalidNameException, InvalidDescriptionException {
        setName(name);
        setDescription(description);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) throws InvalidNameException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException(name, "Le nom de la spécialité ne peut pas être vide");
        }
        this.name = name;
    }

    public void setDescription(String description) throws InvalidDescriptionException {
        if (description == null || description.trim().isEmpty()) {
            throw new InvalidDescriptionException(description, "La description ne peut pas être vide");
        }
        this.description = description;
    }
}