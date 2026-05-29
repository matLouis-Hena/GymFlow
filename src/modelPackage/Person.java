package modelPackage;

import exceptionPackage.*;
import java.time.LocalDate;

public class Person {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Gender gender;
    private String email;
    private String phone;
    private Integer lockerNumber;
    private String username;
    private String password;

    public Person(int id, String firstName, String lastName, LocalDate birthDate, Gender gender, String email, String phone, Integer lockerNumber, String username, String password)
            throws InvalidFirstNameException, InvalidLastNameException, InvalidGenderException, InvalidEmailException, InvalidPhoneException, InvalidLockerNumberException, InvalidUsernameException, InvalidPasswordException {
        this.id = id;
        setFirstName(firstName);
        setLastName(lastName);
        setBirthDate(birthDate);
        setGender(gender);
        setEmail(email);
        setPhone(phone);
        setLockerNumber(lockerNumber);
        setUsername(username);
        setPassword(password);
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getLockerNumber() {
        return lockerNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String firstName) throws InvalidFirstNameException {
        if (firstName == null || firstName.trim().isEmpty()) {
            String message = "Le prénom ne peut pas être vide !";
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

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setGender(Gender gender) throws InvalidGenderException {
        if(gender == null) {
            throw new InvalidGenderException(
                    null,
                    "Le genre est obligatoire"
            );
        }
        this.gender = gender;
    }

    public void setEmail(String email) throws InvalidEmailException {
        if (email == null || !email.matches("^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$")) {
            String message = "La valeur " + email + " proposée pour l'email est invalide";
            throw new InvalidEmailException(email, message);
        }
        this.email = email;
    }

    public void setPhone(String phone) throws InvalidPhoneException {
        if (phone != null) {
            // Conversion automatique 0 → +32
            if (phone.startsWith("0")) {
                phone = "+32" + phone.substring(1);
            }
            if (!phone.matches("^\\+324[56789]\\d{7}$")) {
                String message = "La valeur " + phone + " proposée pour le numéro de téléphone est invalide";
                throw new InvalidPhoneException(phone, message);
            }
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
}