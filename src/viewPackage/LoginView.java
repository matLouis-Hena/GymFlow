package viewPackage;

import java.io.InputStream;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoginView {

    private final BorderPane root;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button loginButton;
    private final Button createAccountButton;

    public LoginView() {
        root = new BorderPane();
        usernameField = new TextField();
        passwordField = new PasswordField();
        loginButton = new Button("Se connecter");
        createAccountButton = new Button("Creer un compte");

        createForm();
    }

    public Parent getRoot() {
        return root;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getCreateAccountButton() {
        return createAccountButton;
    }

    public String getUsername() {
        return getRequiredText(usernameField, "Le nom d'utilisateur est obligatoire.");
    }

    public String getPassword() {
        return getRequiredText(passwordField, "Le mot de passe est obligatoire.");
    }

    private void createForm() {
        Node logoView = createLogoView();
        logoView.setTranslateY(-170);

        Label titleLabel = new Label("Connexion");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.CENTER);

        formGrid.add(new Label("Nom d'utilisateur *"), 0, 0);
        formGrid.add(usernameField, 1, 0);
        formGrid.add(new Label("Mot de passe *"), 0, 1);
        formGrid.add(passwordField, 1, 1);

        HBox buttonBox = new HBox(10, loginButton, createAccountButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox container = new VBox(20, titleLabel, formGrid, buttonBox);
        container.setPadding(new Insets(30));
        container.setAlignment(Pos.CENTER);

        StackPane centerPane = new StackPane();
        centerPane.getChildren().add(logoView);
        centerPane.getChildren().add(container);

        StackPane.setAlignment(logoView, Pos.CENTER);
        StackPane.setAlignment(container, Pos.CENTER);

        root.setCenter(centerPane);
    }

    private Node createLogoView() {
        InputStream logoStream = getClass().getResourceAsStream("/images/logo_salle.png");

        if (logoStream == null) {
            return new Label("");
        }

        ImageView logoView = new ImageView(new Image(logoStream));
        logoView.setFitWidth(320);
        logoView.setPreserveRatio(true);
        logoView.setSmooth(true);

        return logoView;
    }

    private String getRequiredText(TextField textField, String message) {
        String value = textField.getText();

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }

        return value.trim();
    }
}
