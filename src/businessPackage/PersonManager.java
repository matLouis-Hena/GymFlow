package businessPackage;

import dataAccessPackage.personDataAccess.IPersonDA;
import dataAccessPackage.personDataAccess.PersonDBAccess;
import exceptionPackage.person.AddPersonException;
import exceptionPackage.person.DeletePersonException;
import exceptionPackage.person.ReadPersonException;
import exceptionPackage.person.UpdatePersonException;
import modelPackage.Person;

import java.time.LocalDate;
import java.util.List;

public class PersonManager {

    private final IPersonDA personDataAccess;

    public PersonManager() {
        this.personDataAccess = new PersonDBAccess();
    }

    public PersonManager(IPersonDA personDataAccess) {
        this.personDataAccess = personDataAccess;
    }

    public int createPerson(Person person) throws AddPersonException {
        validatePersonForAdd(person);
        return personDataAccess.insert(person);
    }

    public List<Person> getAllPersons() throws ReadPersonException {
        return personDataAccess.getAll();
    }

    public Person getPersonById(int id) throws ReadPersonException {
        validateIdForRead(id);

        Person person = personDataAccess.getById(id);

        if (person == null) {
            throw new ReadPersonException(
                    String.valueOf(id),
                    "Aucune personne trouvee avec cet identifiant."
            );
        }

        return person;
    }

    public void updatePerson(Person person) throws UpdatePersonException {
        validatePersonForUpdate(person);
        personDataAccess.update(person);
    }

    public void deletePerson(int id) throws DeletePersonException {
        validateIdForDelete(id);
        personDataAccess.delete(id);
    }

    private void validatePersonForAdd(Person person) throws AddPersonException {
        if (person == null) {
            throw new AddPersonException("person", "La personne ne peut pas etre vide.");
        }

        validateRequiredFieldsForAdd(person);
        validateBirthDateForAdd(person);
    }

    private void validatePersonForUpdate(Person person) throws UpdatePersonException {
        if (person == null) {
            throw new UpdatePersonException("person", "La personne ne peut pas etre vide.");
        }

        if (person.getId() <= 0) {
            throw new UpdatePersonException(
                    String.valueOf(person.getId()),
                    "L'identifiant de la personne est invalide."
            );
        }

        validateRequiredFieldsForUpdate(person);
        validateBirthDateForUpdate(person);
    }

    private void validateRequiredFieldsForAdd(Person person) throws AddPersonException {
        if (person.getFirstName() == null || person.getFirstName().isBlank()) {
            throw new AddPersonException("firstName", "Le prenom est obligatoire.");
        }

        if (person.getLastName() == null || person.getLastName().isBlank()) {
            throw new AddPersonException("lastName", "Le nom est obligatoire.");
        }

        if (person.getBirthDate() == null) {
            throw new AddPersonException("birthDate", "La date de naissance est obligatoire.");
        }

        if (person.getGender() == null) {
            throw new AddPersonException("gender", "Le genre est obligatoire.");
        }

        if (person.getEmail() == null || person.getEmail().isBlank()) {
            throw new AddPersonException("email", "L'email est obligatoire.");
        }

        if (person.getUsername() == null || person.getUsername().isBlank()) {
            throw new AddPersonException("username", "Le nom d'utilisateur est obligatoire.");
        }

        if (person.getPassword() == null || person.getPassword().isBlank()) {
            throw new AddPersonException("password", "Le mot de passe est obligatoire.");
        }
    }

    private void validateRequiredFieldsForUpdate(Person person) throws UpdatePersonException {
        if (person.getFirstName() == null || person.getFirstName().isBlank()) {
            throw new UpdatePersonException(String.valueOf(person.getId()), "Le prenom est obligatoire.");
        }

        if (person.getLastName() == null || person.getLastName().isBlank()) {
            throw new UpdatePersonException(String.valueOf(person.getId()), "Le nom est obligatoire.");
        }

        if (person.getBirthDate() == null) {
            throw new UpdatePersonException(String.valueOf(person.getId()), "La date de naissance est obligatoire.");
        }

        if (person.getGender() == null) {
            throw new UpdatePersonException(String.valueOf(person.getId()), "Le genre est obligatoire.");
        }

        if (person.getEmail() == null || person.getEmail().isBlank()) {
            throw new UpdatePersonException(String.valueOf(person.getId()), "L'email est obligatoire.");
        }

        if (person.getUsername() == null || person.getUsername().isBlank()) {
            throw new UpdatePersonException(String.valueOf(person.getId()), "Le nom d'utilisateur est obligatoire.");
        }

        if (person.getPassword() == null || person.getPassword().isBlank()) {
            throw new UpdatePersonException(String.valueOf(person.getId()), "Le mot de passe est obligatoire.");
        }
    }

    private void validateBirthDateForAdd(Person person) throws AddPersonException {
        if (person.getBirthDate().isAfter(LocalDate.now())) {
            throw new AddPersonException("birthDate", "La date de naissance ne peut pas etre dans le futur.");
        }
    }

    private void validateBirthDateForUpdate(Person person) throws UpdatePersonException {
        if (person.getBirthDate().isAfter(LocalDate.now())) {
            throw new UpdatePersonException(String.valueOf(person.getId()), "La date de naissance ne peut pas etre dans le futur.");
        }
    }

    private void validateIdForRead(int id) throws ReadPersonException {
        if (id <= 0) {
            throw new ReadPersonException(String.valueOf(id), "L'identifiant doit etre superieur a 0.");
        }
    }

    private void validateIdForDelete(int id) throws DeletePersonException {
        if (id <= 0) {
            throw new DeletePersonException(String.valueOf(id), "L'identifiant doit etre superieur a 0.");
        }
    }
}
