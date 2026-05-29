package viewPackage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import modelPackage.Gender;
import modelPackage.GymMember;
import modelPackage.Subscription;
import modelPackage.SubscriptionType;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class GymMemberFormView {

    private final BorderPane root;
    private final GymMember memberToUpdate;

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField birthDateField;
    private ComboBox<Gender> genderComboBox;
    private TextField emailField;
    private TextField phoneField;
    private TextField lockerNumberField;
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField weightField;
    private TextField heightField;
    private ComboBox<SubscriptionType> subscriptionTypeComboBox;
    private TextField durationField;
    private Label monthlyPriceLabel;
    private Label totalPriceLabel;
    private Button registrationButton;
    private Button cancelButton;

    public GymMemberFormView(GymMember member) {
        root = new BorderPane();
        memberToUpdate = member;

        createFields();
        createForm();

        if (memberToUpdate != null) {
            fillFields();
        }
    }

    public Parent getRoot() {
        return root;
    }

    public Button getRegistrationButton() {
        return registrationButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public boolean isUpdateMode() {
        return memberToUpdate != null;
    }

    public GymMember createGymMember() throws Exception {
        int id = 0;

        if (memberToUpdate != null) {
            id = memberToUpdate.getId();
        }

        String firstName = getRequiredText(firstNameField, "Le prenom est obligatoire.");
        String lastName = getRequiredText(lastNameField, "Le nom est obligatoire.");
        LocalDate birthDate = getBirthDate();
        Gender gender = getGender();
        String email = getRequiredText(emailField, "L'email est obligatoire.");
        String phone = getOptionalText(phoneField);
        Integer lockerNumber = getOptionalInteger(lockerNumberField, "Le numero de casier doit etre un nombre.");
        String username = getRequiredText(usernameField, "Le nom d'utilisateur est obligatoire.");
        String password = getRequiredText(passwordField, "Le mot de passe est obligatoire.");
        boolean isActive = true;
        Double weight = getRequiredDouble(weightField, "Le poids doit etre un nombre.");
        Integer height = getRequiredInteger(heightField, "La taille doit etre un nombre.");
        SubscriptionType subscriptionType = getSubscriptionType();
        Integer durationMonths = getRequiredInteger(durationField, "La duree doit etre un nombre.");

        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La date de naissance ne peut pas etre dans le futur.");
        }

        if (weight <= 0) {
            throw new IllegalArgumentException("Le poids doit etre superieur a 0.");
        }

        if (height <= 0) {
            throw new IllegalArgumentException("La taille doit etre superieure a 0.");
        }

        if (durationMonths <= 0) {
            throw new IllegalArgumentException("La duree de l'abonnement doit etre superieure a 0.");
        }

        Subscription subscription = createSubscription(subscriptionType, durationMonths);

        return new GymMember(
                id,
                firstName,
                lastName,
                birthDate,
                gender,
                email,
                phone,
                lockerNumber,
                username,
                password,
                isActive,
                weight,
                height,
                subscription
        );
    }

    private void createFields() {
        firstNameField = new TextField();
        lastNameField = new TextField();
        birthDateField = new TextField();
        genderComboBox = new ComboBox<>();
        emailField = new TextField();
        phoneField = new TextField();
        lockerNumberField = new TextField();
        usernameField = new TextField();
        passwordField = new PasswordField();
        weightField = new TextField();
        heightField = new TextField();
        subscriptionTypeComboBox = new ComboBox<>();
        durationField = new TextField();
        monthlyPriceLabel = new Label("Prix par mois :");
        totalPriceLabel = new Label("Prix total :");
        registrationButton = new Button("S'inscrire");
        cancelButton = new Button("Annuler");

        genderComboBox.getItems().add(Gender.MALE);
        genderComboBox.getItems().add(Gender.FEMALE);
        genderComboBox.getItems().add(Gender.OTHER);

        subscriptionTypeComboBox.getItems().add(SubscriptionType.BASIC);
        subscriptionTypeComboBox.getItems().add(SubscriptionType.STANDARD);
        subscriptionTypeComboBox.getItems().add(SubscriptionType.PREMIUM);

        durationField.setText("1");

        subscriptionTypeComboBox.setOnAction(event -> updatePriceLabels());
        durationField.setOnKeyReleased(event -> updatePriceLabels());
    }

    private void createForm() {
        Label titleLabel = new Label("Inscription d'un membre");

        if (memberToUpdate != null) {
            titleLabel.setText("Modification d'un membre");
            registrationButton.setText("Enregistrer");
        }

        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setPadding(new Insets(20));
        formGrid.setHgap(15);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.CENTER);

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
        formGrid.add(new Label("Numero de casier"), 0, 6);
        formGrid.add(lockerNumberField, 1, 6);
        formGrid.add(new Label("Nom d'utilisateur *"), 0, 7);
        formGrid.add(usernameField, 1, 7);
        formGrid.add(new Label("Mot de passe *"), 0, 8);
        formGrid.add(passwordField, 1, 8);
        formGrid.add(new Label("Poids *"), 0, 9);
        formGrid.add(weightField, 1, 9);
        formGrid.add(new Label("Taille *"), 0, 10);
        formGrid.add(heightField, 1, 10);
        formGrid.add(new Label("Abonnement *"), 0, 11);
        formGrid.add(subscriptionTypeComboBox, 1, 11);
        formGrid.add(new Label("Duree en mois *"), 0, 12);
        formGrid.add(durationField, 1, 12);
        formGrid.add(monthlyPriceLabel, 1, 13);
        formGrid.add(totalPriceLabel, 1, 14);

        HBox buttonBox = new HBox(10, registrationButton, cancelButton);
        formGrid.add(buttonBox, 1, 15);

        BorderPane container = new BorderPane();
        container.setPadding(new Insets(20));
        container.setTop(titleLabel);
        container.setCenter(formGrid);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        root.setCenter(container);
    }

    private void fillFields() {
        firstNameField.setText(memberToUpdate.getFirstName());
        lastNameField.setText(memberToUpdate.getLastName());
        birthDateField.setText(String.valueOf(memberToUpdate.getBirthDate()));
        genderComboBox.getSelectionModel().select(memberToUpdate.getGender());
        emailField.setText(memberToUpdate.getEmail());
        setNullableText(phoneField, memberToUpdate.getPhone());
        setNullableText(lockerNumberField, memberToUpdate.getLockerNumber());
        usernameField.setText(memberToUpdate.getUsername());
        passwordField.setText(memberToUpdate.getPassword());
        weightField.setText(String.valueOf(memberToUpdate.getWeight()));
        heightField.setText(String.valueOf(memberToUpdate.getHeight()));

        if (memberToUpdate.getEnrollment() != null) {
            subscriptionTypeComboBox.getSelectionModel().select(memberToUpdate.getEnrollment().getType());
            durationField.setText(String.valueOf(memberToUpdate.getEnrollment().getDurationMonths()));
        }

        updatePriceLabels();
    }

    private void updatePriceLabels() {
        SubscriptionType subscriptionType = subscriptionTypeComboBox.getSelectionModel().getSelectedItem();

        if (subscriptionType == null) {
            monthlyPriceLabel.setText("Prix par mois :");
            totalPriceLabel.setText("Prix total :");
            return;
        }

        monthlyPriceLabel.setText("Prix par mois : " + subscriptionType.getMonthlyPrice());

        try {
            int durationMonths = Integer.parseInt(durationField.getText());
            totalPriceLabel.setText("Prix total : " + subscriptionType.calculatePrice(durationMonths));
        } catch (NumberFormatException exception) {
            totalPriceLabel.setText("Prix total :");
        }
    }

    private Subscription createSubscription(SubscriptionType subscriptionType, int durationMonths) throws Exception {
        int subscriptionId = 0;

        if (memberToUpdate != null && memberToUpdate.getEnrollment() != null) {
            subscriptionId = memberToUpdate.getEnrollment().getId();
        }

        return new Subscription(subscriptionId, subscriptionType, durationMonths);
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

    private SubscriptionType getSubscriptionType() {
        SubscriptionType subscriptionType = subscriptionTypeComboBox.getSelectionModel().getSelectedItem();

        if (subscriptionType == null) {
            throw new IllegalArgumentException("Le type d'abonnement est obligatoire.");
        }

        return subscriptionType;
    }

    private Double getRequiredDouble(TextField textField, String message) {
        String value = getRequiredText(textField, message);

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(message);
        }
    }

    private Integer getRequiredInteger(TextField textField, String message) {
        String value = getRequiredText(textField, message);

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(message);
        }
    }

    private Integer getOptionalInteger(TextField textField, String message) {
        String value = getOptionalText(textField);

        if (value == null) {
            return null;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(message);
        }
    }

    private void setNullableText(TextField textField, Object value) {
        if (value == null) {
            textField.setText("");
        } else {
            textField.setText(String.valueOf(value));
        }
    }
}
