package modelPackage;

import exceptionPackage.*;
import java.time.LocalDate;

public class GymMember extends Person {

    private Boolean isActive;
    private double weight;
    private int height;
    private Subscription enrollment;

    public GymMember(int id, String firstName, String lastName, LocalDate birthDate, Gender gender, String email, String phone, Integer lockerNumber, String username, String password,
                     Boolean isActive, Double weight, Integer height, Subscription enrollment)
            throws InvalidFirstNameException, InvalidLastNameException, InvalidGenderException, InvalidEmailException, InvalidPhoneException, InvalidLockerNumberException, InvalidUsernameException, InvalidPasswordException, InvalidWeightException, InvalidHeightException {
        super(id, firstName, lastName, birthDate, gender, email, phone, lockerNumber, username, password);

        setIsActive(isActive);
        setWeight(weight);
        setHeight(height);
        this.enrollment = enrollment;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setWeight(Double weight) throws InvalidWeightException {
        if (weight == null || weight <= 0) {
            String message = "La valeur " + weight + " proposée pour le poids est invalide (doit être > 0)";
            throw new InvalidWeightException(weight, message);
        }
        this.weight = weight;
    }

    public void setHeight(Integer height) throws InvalidHeightException {
        if (height == null || height <= 0) {
            String message = "La valeur " + height + " proposée pour la taille est invalide (doit être > 0)";
            throw new InvalidHeightException(height, message);
        }
        this.height = height;
    }
}