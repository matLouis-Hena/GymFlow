package modelPackage;

import exceptionPackage.*;
import java.time.LocalDate;

public class Admin extends Person {
    private AccessLevel accessLevel;

    public Admin(int id, String firstName, String lastName, LocalDate birthDate,
                 Gender gender, String email, String phone, Integer lockerNumber,
                 String username, String password,
                 AccessLevel accessLevel)
            throws InvalidFirstNameException, InvalidLastNameException,
            InvalidGenderException,
            InvalidEmailException, InvalidPhoneException, InvalidLockerNumberException,
            InvalidUsernameException, InvalidPasswordException {

        super(id, firstName, lastName, birthDate, gender, email, phone, lockerNumber, username, password);

        setAccessLevel(accessLevel);
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        if (accessLevel == null) {
            String message = "Le niveau d'accès ne peut pas être null";
            throw new IllegalArgumentException(message);
        }
        this.accessLevel = accessLevel;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }
}