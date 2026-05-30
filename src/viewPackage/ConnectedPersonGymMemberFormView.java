package viewPackage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import modelPackage.GymMember;
import modelPackage.Person;
import modelPackage.Subscription;
import modelPackage.SubscriptionType;

public class ConnectedPersonGymMemberFormView {

    private final BorderPane root;
    private final Person person;

    private CheckBox wantsLockerCheckBox;
    private TextField weightField;
    private TextField heightField;
    private TextField sponsorUsernameField;
    private ComboBox<SubscriptionType> subscriptionTypeComboBox;
    private TextField durationField;
    private Label monthlyPriceLabel;
    private Label totalPriceLabel;
    private Button registrationButton;
    private Button cancelButton;

    public ConnectedPersonGymMemberFormView(Person person) {
        root = new BorderPane();
        this.person = person;

        createFields();
        createForm();
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

    public String getSponsorUsername() {
        return ViewInputHelper.getOptionalText(sponsorUsernameField);
    }

    public GymMember createGymMember() throws Exception {
        boolean wantsLocker = wantsLockerCheckBox.isSelected();
        Double weight = ViewInputHelper.getRequiredDouble(weightField, "Le poids doit etre un nombre.");
        Integer height = ViewInputHelper.getRequiredInteger(heightField, "La taille doit etre un nombre.");
        SubscriptionType subscriptionType = getSubscriptionType();
        Integer durationMonths = ViewInputHelper.getRequiredInteger(durationField, "La duree doit etre un nombre.");

        if (weight <= 0) {
            throw new IllegalArgumentException("Le poids doit etre superieur a 0.");
        }

        if (height <= 0) {
            throw new IllegalArgumentException("La taille doit etre superieure a 0.");
        }

        if (durationMonths <= 0) {
            throw new IllegalArgumentException("La duree de l'abonnement doit etre superieure a 0.");
        }

        Subscription subscription = new Subscription(0, subscriptionType, durationMonths);

        return new GymMember(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getBirthDate(),
                person.getGender(),
                person.getEmail(),
                person.getPhone(),
                null,
                person.getUsername(),
                person.getPassword(),
                wantsLocker,
                weight,
                height,
                subscription
        );
    }

    private void createFields() {
        wantsLockerCheckBox = new CheckBox();
        weightField = new TextField();
        heightField = new TextField();
        sponsorUsernameField = new TextField();
        subscriptionTypeComboBox = new ComboBox<>();
        durationField = new TextField();
        monthlyPriceLabel = new Label("Prix par mois :");
        totalPriceLabel = new Label("Prix total :");
        registrationButton = new Button("Inscrire a la salle");
        cancelButton = new Button("Annuler");

        subscriptionTypeComboBox.getItems().add(SubscriptionType.BASIC);
        subscriptionTypeComboBox.getItems().add(SubscriptionType.STANDARD);
        subscriptionTypeComboBox.getItems().add(SubscriptionType.PREMIUM);

        durationField.setText("1");

        subscriptionTypeComboBox.setOnAction(event -> updatePriceLabels());
        durationField.setOnKeyReleased(event -> updatePriceLabels());
    }

    private void createForm() {
        Label titleLabel = new Label("Inscription a la salle");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label accountLabel = new Label(
                "Compte : "
                        + person.getFirstName()
                        + " "
                        + person.getLastName()
                        + " ("
                        + person.getUsername()
                        + ")"
        );

        GridPane formGrid = new GridPane();
        formGrid.setPadding(new Insets(20));
        formGrid.setHgap(15);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.CENTER);

        formGrid.add(accountLabel, 0, 0, 2, 1);
        formGrid.add(new Label("Souhaite un casier"), 0, 1);
        formGrid.add(wantsLockerCheckBox, 1, 1);
        formGrid.add(new Label("Poids *"), 0, 2);
        formGrid.add(weightField, 1, 2);
        formGrid.add(new Label("Taille *"), 0, 3);
        formGrid.add(heightField, 1, 3);
        formGrid.add(new Label("Parrain username"), 0, 4);
        formGrid.add(sponsorUsernameField, 1, 4);
        formGrid.add(new Label("Abonnement *"), 0, 5);
        formGrid.add(subscriptionTypeComboBox, 1, 5);
        formGrid.add(new Label("Duree en mois *"), 0, 6);
        formGrid.add(durationField, 1, 6);
        formGrid.add(monthlyPriceLabel, 1, 7);
        formGrid.add(totalPriceLabel, 1, 8);

        HBox buttonBox = new HBox(10, registrationButton, cancelButton);
        formGrid.add(buttonBox, 1, 9);

        BorderPane container = new BorderPane();
        container.setPadding(new Insets(20));
        container.setTop(titleLabel);
        container.setCenter(formGrid);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        root.setCenter(container);
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

    private SubscriptionType getSubscriptionType() {
        SubscriptionType subscriptionType = subscriptionTypeComboBox.getSelectionModel().getSelectedItem();

        if (subscriptionType == null) {
            throw new IllegalArgumentException("Le type d'abonnement est obligatoire.");
        }

        return subscriptionType;
    }

}
