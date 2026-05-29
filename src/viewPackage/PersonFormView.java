package viewPackage;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import modelPackage.Gender;
import modelPackage.Person;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PersonFormView {

    private final BorderPane root;

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField birthDateField;
    private ComboBox<Gender> genderComboBox;
    private TextField emailField;
    private TextField phoneField;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button createAccountButton;
    private Button cancelButton;

    public PersonFormView() {
        root = new BorderPane();
        createFields();
        createForm();
    }

    public Parent getRoot() {
        return root;
    }

    public Button getCreateAccountButton() {
        return createAccountButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public Person createPerson() throws Exception {
        String firstName = getRequiredText(firstNameField, "Le prenom est obligatoire.");
        String lastName = getRequiredText(lastNameField, "Le nom est obligatoire.");
        LocalDate birthDate = getBirthDate();
        Gender gender = getGender();
        String email = getRequiredText(emailField, "L'email est obligatoire.");
        String phone = getOptionalText(phoneField);
        String username = getRequiredText(usernameField, "Le nom d'utilisateur est obligatoire.");
        String password = getRequiredText(passwordField, "Le mot de passe est obligatoire.");

        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La date de naissance ne peut pas etre dans le futur.");
        }

        return new Person(
                0,
                firstName,
                lastName,
                birthDate,
                gender,
                email,
                phone,
                null,
                username,
                password
        );
    }

    private void createFields() {
        firstNameField = new TextField();
        lastNameField = new TextField();
        birthDateField = new TextField();
        genderComboBox = new ComboBox<>();
        emailField = new TextField();
        phoneField = new TextField();
        usernameField = new TextField();
        passwordField = new PasswordField();
        createAccountButton = new Button("Creer le compte");
        cancelButton = new Button("Annuler");

        genderComboBox.getItems().add(Gender.MALE);
        genderComboBox.getItems().add(Gender.FEMALE);
        genderComboBox.getItems().add(Gender.OTHER);
    }

    private void createForm() {
        Label titleLabel = new Label("Creation d'un compte");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setPadding(new Insets(20));
        formGrid.setHgap(15);
        formGrid.setVgap(10);

        formGrid.add(new Label("Prenom *"), 0, 0);
        formGrid.add(firstNameField, 1, 0);
        formGrid.add(new Label("Nom *"), 0, 1);
        formGrid.add(lastNameField, 1, 1);
        formGrid.add(new Label("Date naissance * (yyyy-mm-dd)"), 0, 2);
        formGrid.add(birthDateField, 1, 2);
        formGrid.add(new Label("Genre *"), 0, 3);
        formGrid.add(genderComboBox, 1, 3);
        formGrid.add(new Label("Email *"), 0, 4);
        formGrid.add(emailField, 1, 4);
        formGrid.add(new Label("Telephone"), 0, 5);
        formGrid.add(phoneField, 1, 5);
        formGrid.add(new Label("Nom d'utilisateur *"), 0, 6);
        formGrid.add(usernameField, 1, 6);
        formGrid.add(new Label("Mot de passe *"), 0, 7);
        formGrid.add(passwordField, 1, 7);

        HBox buttonBox = new HBox(10, createAccountButton, cancelButton);
        formGrid.add(buttonBox, 1, 8);

        BorderPane container = new BorderPane();
        container.setPadding(new Insets(20));
        container.setTop(titleLabel);
        container.setCenter(formGrid);

        root.setCenter(container);
    }

    private String getRequiredText(TextField textField, String message) {
        String value = textField.getText();

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }

    private String getOptionalText(TextField textField) {
        String value = textField.getText();

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return value.trim();
    }

    private LocalDate getBirthDate() {
        String value = getRequiredText(birthDateField, "La date de naissance est obligatoire.");

        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("La date de naissance doit respecter le format yyyy-mm-dd.");
        }
    }

    private Gender getGender() {
        Gender gender = genderComboBox.getSelectionModel().getSelectedItem();

        if (gender == null) {
            throw new IllegalArgumentException("Le genre est obligatoire.");
        }

        return gender;
    }
}
