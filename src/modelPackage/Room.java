package modelPackage;

import exceptionPackage.validation.InvalidCapacityException;
import exceptionPackage.validation.InvalidNameException;

public class Room {
    private int id;
    private String name;
    private int capacity;

    public Room(int id, String name, int capacity) throws InvalidCapacityException, InvalidNameException {
        this.id = id;
        setName(name);
        setCapacity(capacity);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setName(String name) throws InvalidNameException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException(name, "Le nom de la salle ne peut pas être vide");
        }
        this.name = name;
    }

    public void setCapacity(int capacity) throws InvalidCapacityException {
        if (capacity <= 0) {
            throw new InvalidCapacityException(capacity, "La capacité doit être supérieure à 0");
        }
        this.capacity = capacity;
    }
}