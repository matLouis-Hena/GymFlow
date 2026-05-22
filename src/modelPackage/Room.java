package modelPackage;

import exceptionPackage.*;

public class Room {
    private int id;
    private String name;
    private int capacity;

    public Room(int id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCapacity(int capacity) throws InvalidCapacityException {
        if (capacity <= 0) {
            throw new InvalidCapacityException(capacity, "La capacité doit être supérieure à 0");
        }
        this.capacity = capacity;
    }
}