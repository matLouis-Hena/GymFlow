package controllerPackage;

import businessPackage.PersonManager;
import exceptionPackage.person.AddPersonException;
import exceptionPackage.person.ReadPersonException;
import modelPackage.Person;
import modelPackage.UserRole;
import viewPackage.MainView;

import java.util.List;

public class PersonController {

    private final PersonManager personManager;
    private final MainView mainView;

    public PersonController(PersonManager personManager, MainView mainView) {
        this.personManager = personManager;
        this.mainView = mainView;
    }

    public void showCreateAccountForm() {
        mainView.showPersonForm();
    }

    public void showCreateAccountFormForLogin() {
        mainView.showPersonFormForLogin();
    }

    public void login(String username, String password) {
        try {
            Person person = findPersonByUsernameAndPassword(username, password);

            if (person == null) {
                mainView.showErrorMessage("Nom d'utilisateur ou mot de passe incorrect.");
                return;
            }

            UserRole userRole = personManager.getUserRoleByPersonId(person.getId());
            mainView.login(person, userRole);

        } catch (ReadPersonException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void showPersons() {
        try {
            List<Person> persons = personManager.getAllPersons();
            mainView.showPersonList(persons);
        } catch (ReadPersonException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void createPerson(Person person) {
        try {
            personManager.createPerson(person);
            mainView.showInformationMessage("Compte cree avec succes.");
            showPersons();
        } catch (AddPersonException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void createPersonForLogin(Person person) {
        try {
            int id = personManager.createPerson(person);
            Person createdPerson = personManager.getPersonById(id);
            UserRole userRole = personManager.getUserRoleByPersonId(id);
            mainView.showInformationMessage("Compte cree avec succes.");
            mainView.login(createdPerson, userRole);
        } catch (AddPersonException | ReadPersonException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    private Person findPersonByUsernameAndPassword(String username, String password)
            throws ReadPersonException {
        List<Person> persons = personManager.getAllPersons();

        for (Person person : persons) {
            if (person.getUsername().equals(username) && person.getPassword().equals(password)) {
                return person;
            }
        }

        return null;
    }
}
