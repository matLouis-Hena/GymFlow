package modelPackage;

import exceptionPackage.*;
import java.time.LocalDate;

public class Person {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private char gender;
    private String email;
    private String phone;
    private Integer lockerNumber;
    private String username;
    private String password;

    public Person(int id, String firstName, String lastName, LocalDate birthDate, char gender, String email, String phone, Integer lockerNumber, String username, String password)
            throws InvalidFirstNameException, InvalidLastNameException, InvalidGenderException, InvalidEmailException, InvalidPhoneException, InvalidLockerNumberException, InvalidUsernameException, InvalidPasswordException {
        this.id = id;
        setFirstName(firstName);
        setLastName(lastName);
        this.birthDate = birthDate;
        setGender(gender);
        setEmail(email);
        setPhone(phone);
        setLockerNumber(lockerNumber);
        setUsername(username);
        setPassword(password);
    }

    // Setters
    public void setFirstName(String firstName) throws InvalidFirstNameException {
        if (firstName == null || firstName.trim().isEmpty()) {
            String message = "Le prénom de peut pas être vide !";
            throw new InvalidFirstNameException(firstName, message);
        }
        this.firstName = firstName;
    }

    public void setLastName(String lastName) throws InvalidLastNameException {
        if (lastName == null || lastName.trim().isEmpty()) {
            String message = "Le nom de famille ne peut pas être vide !";
            throw new InvalidLastNameException(lastName, message);
        }
        this.lastName = lastName;
    }

    public void setGender(char gender) throws InvalidGenderException {
        if (gender != 'm' && gender != 'f' && gender != 'x') {
            String message = "La valeur " + gender + " proposée pour le genre est invalide (valeurs acceptées : m, f, x)";
            throw new InvalidGenderException(gender, message);
        }
        this.gender = gender;
    }

    public void setEmail(String email) throws InvalidEmailException {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            String message = "La valeur " + email + " proposée pour l'email est invalide";
            throw new InvalidEmailException(email, message);
        }
        this.email = email;
    }

    public void setPhone(String phone) throws InvalidPhoneException {
        if (phone != null && !phone.matches("[0-9+\\s]{7,20}")) {
            String message = "La valeur " + phone + " proposée pour le numéro de téléphone est invalide";
            throw new InvalidPhoneException(phone, message);
        }
        this.phone = phone;
    }

    public void setLockerNumber(Integer lockerNumber) throws InvalidLockerNumberException {
        if (lockerNumber != null && lockerNumber <= 0) {
            String message = "La valeur " + lockerNumber + " proposée pour le numéro de casier est invalide (doit être > 0)";
            throw new InvalidLockerNumberException(lockerNumber, message);
        }
        this.lockerNumber = lockerNumber;
    }

    public void setUsername(String username) throws InvalidUsernameException {
        if (username == null || username.trim().length() < 3) {
            String message = "La valeur " + username + " proposée pour le nom d'utilisateur est invalide (minimum 3 caractères)";
            throw new InvalidUsernameException(username, message);
        }
        this.username = username;
    }

    public void setPassword(String password) throws InvalidPasswordException {
        if (password == null || password.length() < 8) {
            String message = "Le mot de passe est invalide (minimum 8 caractères)";
            throw new InvalidPasswordException(message);
        }
        this.password = password;
    }

    // Getters

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}