package modelPackage;

import exceptionPackage.*;
import java.time.LocalDate;

public class Coach extends Person {
    private Boolean hasDegree;

    public Coach(int id, String firstName, String lastName, LocalDate birthDate,
                 Gender gender, String email, String phone, Integer lockerNumber,
                 String username, String password,
                 Boolean hasDegree)
            throws InvalidFirstNameException, InvalidLastNameException,
            InvalidGenderException,
            InvalidEmailException, InvalidPhoneException, InvalidLockerNumberException,
            InvalidUsernameException, InvalidPasswordException {

        super(id, firstName, lastName, birthDate, gender, email, phone, lockerNumber, username, password);

        this.hasDegree = hasDegree;
    }

    public void setHasDegree(Boolean hasDegree) {
        this.hasDegree = hasDegree;
    }
}