package modelPackage;

import exceptionPackage.*;
import java.time.LocalDate;

public class Coach extends Person {
    private Boolean has_degree;

    public Coach(int id, String firstName, String lastName, LocalDate birthDate,
                 char gender, String email, String phone, Integer lockerNumber,
                 String username, String password,
                 Boolean has_degree)
            throws InvalidFirstNameException, InvalidLastNameException,
            InvalidGenderException,
            InvalidEmailException, InvalidPhoneException, InvalidLockerNumberException,
            InvalidUsernameException, InvalidPasswordException {

        super(id, firstName, lastName, birthDate, gender, email, phone, lockerNumber, username, password);

        this.has_degree = has_degree;
    }

    public Boolean getHas_degree() {
        return has_degree;
    }
    public void setHas_degree(Boolean has_degree) {
        this.has_degree = has_degree;
    }
}