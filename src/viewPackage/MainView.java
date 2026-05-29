package viewPackage;

import controllerPackage.GymMemberController;
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

import java.util.List;

public class MainView {

    private final BorderPane root;
    private GymMemberController gymMemberController;

    public MainView() {
        root = new BorderPane();

        root.setTop(createMenuBar());
        showWelcomeMessage();
    }

    public Parent getRoot() {
        return root;
    }

    public void setGymMemberController(GymMemberController gymMemberController) {
        this.gymMemberController = gymMemberController;
    }

    public void showGymMemberList(List<GymMember> members) {
        GymMemberListView gymMemberListView = new GymMemberListView(members);

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

    public void showWelcomeMessage() {
        Label titleLabel = new Label("Bienvenue dans l'application de gestion de salle de sport");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label instructionLabel = new Label("Utilisez le menu pour accéder aux fonctionnalités.");

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
        homeMenu.getItems().add(welcomeItem);

        Menu memberMenu = new Menu("Membres");

        MenuItem listMembersItem = new MenuItem("Lister les membres");
        listMembersItem.setOnAction(event -> {
            if (gymMemberController != null) {
                gymMemberController.showMembers();
            }
        });

        memberMenu.getItems().add(listMembersItem);

        menuBar.getMenus().addAll(homeMenu, memberMenu);

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