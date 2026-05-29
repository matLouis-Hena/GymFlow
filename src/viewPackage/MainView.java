package viewPackage;

import controllerPackage.GymMemberController;
import controllerPackage.PersonController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import modelPackage.GymMember;
import modelPackage.Person;

import java.util.List;

public class MainView {

    private final BorderPane root;
    private GymMemberController gymMemberController;
    private PersonController personController;
    private Person connectedPerson;

    public MainView() {
        root = new BorderPane();
        showLoginForm();
    }

    public Parent getRoot() {
        return root;
    }

    public void setGymMemberController(GymMemberController gymMemberController) {
        this.gymMemberController = gymMemberController;
    }

    public void setPersonController(PersonController personController) {
        this.personController = personController;
    }

    public Person getConnectedPerson() {
        return connectedPerson;
    }

    public void login(Person person) {
        connectedPerson = person;
        root.setTop(createMenuBar());
        showWelcomeMessage();
    }

    public void logout() {
        connectedPerson = null;
        root.setTop(null);
        showLoginForm();
    }

    public void showLoginForm() {
        LoginView loginView = new LoginView();

        loginView.getLoginButton().setOnAction(event -> {
            try {
                if (personController != null) {
                    personController.login(loginView.getUsername(), loginView.getPassword());
                }
            } catch (Exception exception) {
                showErrorMessage(exception.getMessage());
            }
        });

        loginView.getCreateAccountButton().setOnAction(event -> {
            if (personController != null) {
                personController.showCreateAccountFormForLogin();
            }
        });

        root.setCenter(loginView.getRoot());
    }

    public void showPersonForm() {
        PersonFormView personFormView = new PersonFormView();

        personFormView.getCreateAccountButton().setOnAction(event -> {
            try {
                Person person = personFormView.createPerson();

                if (personController != null) {
                    personController.createPerson(person);
                }
            } catch (Exception exception) {
                showErrorMessage(exception.getMessage());
            }
        });

        personFormView.getCancelButton().setOnAction(event -> showWelcomeMessage());

        root.setCenter(personFormView.getRoot());
    }

    public void showPersonFormForLogin() {
        PersonFormView personFormView = new PersonFormView();

        personFormView.getCreateAccountButton().setOnAction(event -> {
            try {
                Person person = personFormView.createPerson();

                if (personController != null) {
                    personController.createPersonForLogin(person);
                }
            } catch (Exception exception) {
                showErrorMessage(exception.getMessage());
            }
        });

        personFormView.getCancelButton().setOnAction(event -> showLoginForm());

        root.setCenter(personFormView.getRoot());
    }

    public void showPersonList(List<Person> persons) {
        PersonListView personListView = new PersonListView(persons);

        personListView.getRefreshButton().setOnAction(event -> {
            if (personController != null) {
                personController.showPersons();
            }
        });

        root.setCenter(personListView.getRoot());
    }

    public void showGymMemberList(List<GymMember> members) {
        GymMemberListView gymMemberListView = new GymMemberListView(members);

        gymMemberListView.getAddButton().setOnAction(event -> {
            if (gymMemberController != null) {
                gymMemberController.showConnectedPersonRegistrationForm();
            }
        });

        gymMemberListView.getUpdateButton().setOnAction(event -> {
            if (gymMemberController != null) {
                gymMemberController.showUpdateMemberForm(gymMemberListView.getSelectedMember());
            }
        });

        gymMemberListView.getRefreshButton().setOnAction(event -> {
            if (gymMemberController != null) {
                gymMemberController.showMembers();
            }
        });

        gymMemberListView.getDeleteButton().setOnAction(event -> {
            if (gymMemberController != null) {
                gymMemberController.deleteSelectedMember(gymMemberListView.getSelectedMember());
            }
        });

        root.setCenter(gymMemberListView.getRoot());
    }

    public void showConnectedPersonGymMemberForm() {
        if (connectedPerson == null) {
            showErrorMessage("Veuillez d'abord vous connecter.");
            showLoginForm();
            return;
        }

        ConnectedPersonGymMemberFormView gymMemberFormView =
                new ConnectedPersonGymMemberFormView(connectedPerson);

        gymMemberFormView.getRegistrationButton().setOnAction(event -> {
            try {
                GymMember member = gymMemberFormView.createGymMember();

                if (gymMemberController != null) {
                    gymMemberController.addExistingAccountMember(member);
                }
            } catch (Exception exception) {
                showErrorMessage(exception.getMessage());
            }
        });

        gymMemberFormView.getCancelButton().setOnAction(event -> showWelcomeMessage());

        root.setCenter(gymMemberFormView.getRoot());
    }

    public void showGymMemberForm(GymMember member) {
        GymMemberFormView gymMemberFormView = new GymMemberFormView(member);

        gymMemberFormView.getRegistrationButton().setOnAction(event -> {
            try {
                GymMember formMember = gymMemberFormView.createGymMember();

                if (gymMemberController != null) {
                    if (gymMemberFormView.isUpdateMode()) {
                        gymMemberController.updateMember(formMember);
                    } else {
                        gymMemberController.addMember(formMember);
                    }
                }
            } catch (Exception exception) {
                showErrorMessage(exception.getMessage());
            }
        });

        gymMemberFormView.getCancelButton().setOnAction(event -> {
            if (gymMemberController != null) {
                gymMemberController.showMembers();
            } else {
                showWelcomeMessage();
            }
        });

        root.setCenter(gymMemberFormView.getRoot());
    }

    public void showWelcomeMessage() {
        String firstName = "";

        if (connectedPerson != null) {
            firstName = " " + connectedPerson.getFirstName();
        }

        Label titleLabel = new Label("Bienvenue" + firstName);
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label instructionLabel = new Label("Utilisez le menu pour acceder aux fonctionnalites.");

        VBox welcomeBox = new VBox(15, titleLabel, instructionLabel);
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.setPadding(new Insets(30));

        root.setCenter(welcomeBox);
    }

    public void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showInformationMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu homeMenu = new Menu("Accueil");
        MenuItem welcomeItem = new MenuItem("Afficher l'accueil");
        welcomeItem.setOnAction(event -> showWelcomeMessage());

        MenuItem logoutItem = new MenuItem("Se deconnecter");
        logoutItem.setOnAction(event -> logout());

        homeMenu.getItems().add(welcomeItem);
        homeMenu.getItems().add(logoutItem);

        Menu accountMenu = new Menu("Comptes");

        MenuItem listAccountsItem = new MenuItem("Lister les comptes");
        listAccountsItem.setOnAction(event -> {
            if (personController != null) {
                personController.showPersons();
            }
        });

        accountMenu.getItems().add(listAccountsItem);

        Menu memberMenu = new Menu("Membres");

        MenuItem registrationItem = new MenuItem("Inscription");
        registrationItem.setOnAction(event -> {
            if (gymMemberController != null) {
                gymMemberController.showConnectedPersonRegistrationForm();
            }
        });

        MenuItem listMembersItem = new MenuItem("Lister les membres");
        listMembersItem.setOnAction(event -> {
            if (gymMemberController != null) {
                gymMemberController.showMembers();
            }
        });

        memberMenu.getItems().add(registrationItem);
        memberMenu.getItems().add(listMembersItem);

        menuBar.getMenus().addAll(homeMenu, accountMenu, memberMenu);

        return menuBar;
    }

    public boolean askConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert.showAndWait()
                .filter(buttonType -> buttonType.getButtonData().isDefaultButton())
                .isPresent();
    }
}
