package viewPackage;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import modelPackage.GymMember;

import java.util.List;

public class GymMemberListView {

    private final BorderPane root;
    private final TableView<GymMember> memberTable;
    private final Button addButton;
    private final Button refreshButton;
    private final Button updateButton;
    private final Button deleteButton;

    public GymMemberListView(List<GymMember> members) {
        root = new BorderPane();
        memberTable = new TableView<>();
        addButton = new Button("Inscription");
        refreshButton = new Button("Rafraichir");
        updateButton = new Button("Modifier");
        deleteButton = new Button("Supprimer le membre selectionne");

        createColumns();

        memberTable.setItems(FXCollections.observableArrayList(members));
        memberTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        memberTable.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Label titleLabel = new Label("Liste des membres");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        HBox buttonBox = new HBox(10, addButton, updateButton, refreshButton, deleteButton);

        VBox container = new VBox(15, titleLabel, memberTable, buttonBox);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(memberTable, Priority.ALWAYS);

        root.setCenter(container);
    }

    public Parent getRoot() {
        return root;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public GymMember getSelectedMember() {
        return memberTable.getSelectionModel().getSelectedItem();
    }

    private void createColumns() {
        TableColumn<GymMember, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getId())
        );

        TableColumn<GymMember, String> firstNameColumn = new TableColumn<>("Prenom");
        firstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirstName())
        );

        TableColumn<GymMember, String> lastNameColumn = new TableColumn<>("Nom");
        lastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLastName())
        );

        TableColumn<GymMember, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmail())
        );

        TableColumn<GymMember, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUsername())
        );

        TableColumn<GymMember, Boolean> activeColumn = new TableColumn<>("Actif");
        activeColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getIsActive())
        );

        TableColumn<GymMember, Double> weightColumn = new TableColumn<>("Poids");
        weightColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getWeight())
        );

        TableColumn<GymMember, Integer> heightColumn = new TableColumn<>("Taille");
        heightColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getHeight())
        );

        TableColumn<GymMember, String> subscriptionColumn = new TableColumn<>("Abonnement");
        subscriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEnrollment().toString())
        );

        memberTable.getColumns().add(idColumn);
        memberTable.getColumns().add(firstNameColumn);
        memberTable.getColumns().add(lastNameColumn);
        memberTable.getColumns().add(emailColumn);
        memberTable.getColumns().add(usernameColumn);
        memberTable.getColumns().add(activeColumn);
        memberTable.getColumns().add(weightColumn);
        memberTable.getColumns().add(heightColumn);
        memberTable.getColumns().add(subscriptionColumn);
    }
}
