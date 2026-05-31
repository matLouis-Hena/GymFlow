package modelPackage;

import exceptionPackage.validation.*;

import java.time.LocalDate;

public class Coach extends Person {
    private boolean hasDegree;

    public Coach(int id, String firstName, String lastName, LocalDate birthDate,
                 Gender gender, String email, String phone, Integer lockerNumber,
                 String username, String password,
                 boolean hasDegree)
            throws InvalidFirstNameException, InvalidLastNameException,
            InvalidGenderException,
            InvalidEmailException, InvalidPhoneException, InvalidLockerNumberException,
            InvalidUsernameException, InvalidPasswordException {

        super(id, firstName, lastName, birthDate, gender, email, phone, lockerNumber, username, password);

        this.hasDegree = hasDegree;
    }

    public boolean getHasDegree() {
        return hasDegree;
    }
    public void setHasDegree(boolean hasDegree) {
        this.hasDegree = hasDegree;
    }
}