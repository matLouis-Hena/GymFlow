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
import java.util.Locale;

public class GymMemberFormView {

    private final BorderPane root;
    private final GymMember memberToUpdate;

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField birthDateField;
    private ComboBox<Gender> genderComboBox;
    private TextField emailField;
    private TextField phoneField;
    private CheckBox wantsLockerCheckBox;
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField weightField;
    private TextField heightField;
    private TextField sponsorUsernameField;
    private ComboBox<SubscriptionType> subscriptionTypeComboBox;
    private TextField durationField;
    private Label monthlyPriceLabel;
    private Label totalPriceLabel;
    private Label accessDescriptionLabel;
    private Button registrationButton;
    private Button cancelButton;

    public GymMemberFormView(GymMember member) {
        root = new BorderPane();
        memberToUpdate = member;

        createFields();
        createForm();

        if (memberToUpdate != null) {
            fillFields();
        } else {
            updatePriceLabels();
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

    public String getSponsorUsername() {
        if (sponsorUsernameField == null) {
            return null;
        }

        return ViewInputHelper.getOptionalText(sponsorUsernameField);
    }

    public GymMember createGymMember() throws Exception {
        int id = 0;

        if (memberToUpdate != null) {
            id = memberToUpdate.getId();
        }

        String firstName = ViewInputHelper.getRequiredText(firstNameField, "Le prénom est obligatoire.");
        String lastName = ViewInputHelper.getRequiredText(lastNameField, "Le nom est obligatoire.");
        LocalDate birthDate = getBirthDate();
        Gender gender = getGender();
        String email = ViewInputHelper.getRequiredText(emailField, "L'email est obligatoire.");
        String phone = ViewInputHelper.getOptionalText(phoneField);
        boolean wantsLocker = wantsLockerCheckBox.isSelected();
        Integer lockerNumber = null;

        if (memberToUpdate != null && wantsLocker) {
            lockerNumber = memberToUpdate.getLockerNumber();
        }

        String username = ViewInputHelper.getRequiredText(usernameField, "Le nom d'utilisateur est obligatoire.");
        String password = getPassword();
        Double weight = ViewInputHelper.getRequiredDouble(weightField, "Le poids doit être un nombre.");
        Integer height = ViewInputHelper.getRequiredInteger(heightField, "La taille doit être un nombre.");
        SubscriptionType subscriptionType = getSubscriptionType();
        Integer durationMonths = ViewInputHelper.getRequiredInteger(durationField, "La durée doit être un nombre.");

        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La date de naissance ne peut pas être dans le futur.");
        }

        if (weight <= 0) {
            throw new IllegalArgumentException("Le poids doit être supérieur à 0.");
        }

        if (height <= 0) {
            throw new IllegalArgumentException("La taille doit être supérieure à 0.");
        }

        if (durationMonths <= 0) {
            throw new IllegalArgumentException("La durée de l'abonnement doit être supérieure à 0.");
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
                wantsLocker,
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
        wantsLockerCheckBox = new CheckBox();
        usernameField = new TextField();
        passwordField = new PasswordField();
        weightField = new TextField();
        heightField = new TextField();
        sponsorUsernameField = new TextField();
        subscriptionTypeComboBox = new ComboBox<>();
        durationField = new TextField();
        monthlyPriceLabel = new Label("Prix par mois :");
        totalPriceLabel = new Label("Prix total :");
        accessDescriptionLabel = new Label("Accès :");
        registrationButton = new Button("S'inscrire");
        cancelButton = new Button("Annuler");

        firstNameField.setPromptText("Prénom");
        lastNameField.setPromptText("Nom");
        birthDateField.setPromptText("yyyy-mm-dd");
        emailField.setPromptText("adresse@email.com");
        phoneField.setPromptText("Optionnel");
        usernameField.setPromptText("Nom d'utilisateur");
        passwordField.setPromptText("Mot de passe");
        weightField.setPromptText("Exemple : 75");
        heightField.setPromptText("Exemple : 180");
        sponsorUsernameField.setPromptText("Optionnel");
        durationField.setPromptText("Durée en mois");

        genderComboBox.getItems().add(Gender.MALE);
        genderComboBox.getItems().add(Gender.FEMALE);
        genderComboBox.getItems().add(Gender.OTHER);

        subscriptionTypeComboBox.getItems().add(SubscriptionType.BASIC);
        subscriptionTypeComboBox.getItems().add(SubscriptionType.STANDARD);
        subscriptionTypeComboBox.getItems().add(SubscriptionType.PREMIUM);

        subscriptionTypeComboBox.getSelectionModel().select(SubscriptionType.BASIC);
        durationField.setText("1");

        subscriptionTypeComboBox.setOnAction(event -> updatePriceLabels());
        durationField.setOnKeyReleased(event -> updatePriceLabels());
    }

    private void createForm() {
        Label titleLabel = new Label("Inscription d'un membre");

        if (memberToUpdate != null) {
            titleLabel.setText("Modification d'un membre");
            registrationButton.setText("Enregistrer");
            sponsorUsernameField.setDisable(true);
            sponsorUsernameField.setPromptText("Parrainage uniquement à l'inscription");
        }

        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setPadding(new Insets(20));
        formGrid.setHgap(15);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.CENTER);

        formGrid.add(new Label("Prénom *"), 0, 0);
        formGrid.add(firstNameField, 1, 0);
        formGrid.add(new Label("Nom *"), 0, 1);
        formGrid.add(lastNameField, 1, 1);
        formGrid.add(new Label("Date de naissance * (yyyy-mm-dd)"), 0, 2);
        formGrid.add(birthDateField, 1, 2);
        formGrid.add(new Label("Genre *"), 0, 3);
        formGrid.add(genderComboBox, 1, 3);
        formGrid.add(new Label("Email *"), 0, 4);
        formGrid.add(emailField, 1, 4);
        formGrid.add(new Label("Téléphone"), 0, 5);
        formGrid.add(phoneField, 1, 5);
        formGrid.add(new Label("Souhaite un casier"), 0, 6);
        formGrid.add(wantsLockerCheckBox, 1, 6);
        formGrid.add(new Label("Nom d'utilisateur *"), 0, 7);
        formGrid.add(usernameField, 1, 7);
        formGrid.add(new Label("Mot de passe *"), 0, 8);
        formGrid.add(passwordField, 1, 8);
        formGrid.add(new Label("Poids *"), 0, 9);
        formGrid.add(weightField, 1, 9);
        formGrid.add(new Label("Taille *"), 0, 10);
        formGrid.add(heightField, 1, 10);
        formGrid.add(new Label("Username du parrain"), 0, 11);
        formGrid.add(sponsorUsernameField, 1, 11);
        formGrid.add(new Label("Abonnement *"), 0, 12);
        formGrid.add(subscriptionTypeComboBox, 1, 12);
        formGrid.add(new Label("Durée en mois *"), 0, 13);
        formGrid.add(durationField, 1, 13);
        formGrid.add(monthlyPriceLabel, 1, 14);
        formGrid.add(totalPriceLabel, 1, 15);
        formGrid.add(accessDescriptionLabel, 1, 16);

        HBox buttonBox = new HBox(10, registrationButton, cancelButton);
        formGrid.add(buttonBox, 1, 17);

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
        wantsLockerCheckBox.setSelected(memberToUpdate.getWantsLocker());
        usernameField.setText(memberToUpdate.getUsername());
        passwordField.clear();
        passwordField.setPromptText("Laisser vide pour garder le mot de passe actuel");
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
            accessDescriptionLabel.setText("Accès :");
            return;
        }

        monthlyPriceLabel.setText("Prix par mois : " + formatPrice(subscriptionType.getMonthlyPrice()));

        try {
            int durationMonths = Integer.parseInt(durationField.getText());

            if (durationMonths <= 0) {
                totalPriceLabel.setText("Prix total :");
            } else {
                totalPriceLabel.setText("Prix total : " + formatPrice(subscriptionType.calculatePrice(durationMonths)));
            }

        } catch (NumberFormatException exception) {
            totalPriceLabel.setText("Prix total :");
        }

        accessDescriptionLabel.setText("Accès : " + subscriptionType.getAccessDescription());
    }

    private String getPassword() {
        if (memberToUpdate != null) {
            String enteredPassword = passwordField.getText();

            if (enteredPassword == null || enteredPassword.isBlank()) {
                return memberToUpdate.getPassword();
            }

            return enteredPassword;
        }

        String password = passwordField.getText();

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire.");
        }

        return password;
    }

    private Subscription createSubscription(SubscriptionType subscriptionType, int durationMonths) throws Exception {
        int subscriptionId = 0;

        if (memberToUpdate != null && memberToUpdate.getEnrollment() != null) {
            subscriptionId = memberToUpdate.getEnrollment().getId();
        }

        return new Subscription(subscriptionId, subscriptionType, durationMonths);
    }

    private LocalDate getBirthDate() {
        return ViewInputHelper.getRequiredDate(
                birthDateField,
                "La date de naissance est obligatoire.",
                "La date de naissance doit respecter le format yyyy-mm-dd."
        );
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
            throw new IllegalArgumentException("L'abonnement est obligatoire.");
        }

        return subscriptionType;
    }

    private String formatPrice(double price) {
        return String.format(Locale.FRANCE, "%.2f €", price);
    }

    private void setNullableText(TextField textField, Object value) {
        if (value == null) {
            textField.setText("");
        } else {
            textField.setText(String.valueOf(value));
        }
    }
}