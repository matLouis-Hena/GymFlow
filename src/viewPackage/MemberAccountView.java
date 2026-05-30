package viewPackage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import modelPackage.GymMember;

public class MemberAccountView {

    private final BorderPane root;
    private final GymMember member;
    private final String sponsorText;
    private TextField weightField;
    private TextField heightField;
    private Button saveButton;
    private Button backButton;

    public MemberAccountView(GymMember member, String sponsorText) {
        root = new BorderPane();
        this.member = member;
        this.sponsorText = sponsorText;

        createFields();
        createView();
    }

    public Parent getRoot() {
        return root;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public Button getBackButton() {
        return backButton;
    }

    public GymMember createUpdatedMember() throws Exception {
        Double weight = ViewInputHelper.getRequiredDouble(weightField, "Le poids doit être un nombre.");
        Integer height = ViewInputHelper.getRequiredInteger(heightField, "La taille doit être un nombre.");

        if (weight <= 0) {
            throw new IllegalArgumentException("Le poids doit être superieur à 0.");
        }

        if (height <= 0) {
            throw new IllegalArgumentException("La taille doit être superieure à 0.");
        }

        return new GymMember(
                member.getId(),
                member.getFirstName(),
                member.getLastName(),
                member.getBirthDate(),
                member.getGender(),
                member.getEmail(),
                member.getPhone(),
                member.getLockerNumber(),
                member.getUsername(),
                member.getPassword(),
                member.getWantsLocker(),
                weight,
                height,
                member.getEnrollment()
        );
    }

    private void createFields() {
        weightField = new TextField(String.valueOf(member.getWeight()));
        heightField = new TextField(String.valueOf(member.getHeight()));
        saveButton = new Button("Enregistrer");
        backButton = new Button("Retour");
    }

    private void createView() {
        Label titleLabel = new Label("Mon compte");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(new Label("Prénom"), 0, 0);
        gridPane.add(new Label(member.getFirstName()), 1, 0);
        gridPane.add(new Label("Nom"), 0, 1);
        gridPane.add(new Label(member.getLastName()), 1, 1);
        gridPane.add(new Label("Username"), 0, 2);
        gridPane.add(new Label(member.getUsername()), 1, 2);
        gridPane.add(new Label("Email"), 0, 3);
        gridPane.add(new Label(member.getEmail()), 1, 3);
        gridPane.add(new Label("Téléphone"), 0, 4);
        gridPane.add(new Label(ViewInputHelper.getNullableText(member.getPhone())), 1, 4);
        gridPane.add(new Label("Casier"), 0, 5);
        gridPane.add(new Label(ViewInputHelper.getNullableText(member.getLockerNumber())), 1, 5);
        gridPane.add(new Label("Casier demandé"), 0, 6);
        gridPane.add(new Label(member.getWantsLocker() ? "Oui" : "Non"), 1, 6);
        gridPane.add(new Label("Poids"), 0, 7);
        gridPane.add(weightField, 1, 7);
        gridPane.add(new Label("Taille"), 0, 8);
        gridPane.add(heightField, 1, 8);
        gridPane.add(new Label("Abonnement"), 0, 9);
        gridPane.add(new Label(member.getEnrollment().toString()), 1, 9);
        gridPane.add(new Label("Parrainage"), 0, 10);
        gridPane.add(new Label(sponsorText), 1, 10);

        HBox buttonBox = new HBox(10, saveButton, backButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox container = new VBox(20, titleLabel, gridPane, buttonBox);
        container.setPadding(new Insets(30));
        container.setAlignment(Pos.CENTER);

        root.setCenter(container);
    }

}
