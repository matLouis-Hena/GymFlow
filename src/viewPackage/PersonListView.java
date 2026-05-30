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
import modelPackage.Person;

import java.util.List;

public class PersonListView {

    private final BorderPane root;
    private final TableView<Person> personTable;
    private final Button refreshButton;

    public PersonListView(List<Person> persons) {
        root = new BorderPane();
        personTable = new TableView<>();
        refreshButton = new Button("Rafraîchir");

        createColumns();

        personTable.setItems(FXCollections.observableArrayList(persons));
        personTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        personTable.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Label titleLabel = new Label("Liste des comptes");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        HBox buttonBox = new HBox(10, refreshButton);

        VBox container = new VBox(15, titleLabel, personTable, buttonBox);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(personTable, Priority.ALWAYS);

        root.setCenter(container);
    }

    public Parent getRoot() {
        return root;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    private void createColumns() {
        TableColumn<Person, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getId())
        );

        TableColumn<Person, String> firstNameColumn = new TableColumn<>("Prénom");
        firstNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFirstName())
        );

        TableColumn<Person, String> lastNameColumn = new TableColumn<>("Nom");
        lastNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLastName())
        );

        TableColumn<Person, String> birthDateColumn = new TableColumn<>("Naissance");
        birthDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getBirthDate()))
        );

        TableColumn<Person, String> genderColumn = new TableColumn<>("Genre");
        genderColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getGender()))
        );

        TableColumn<Person, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmail())
        );

        TableColumn<Person, String> phoneColumn = new TableColumn<>("Téléphone");
        phoneColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(ViewInputHelper.getNullableText(cellData.getValue().getPhone()))
        );

        TableColumn<Person, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUsername())
        );

        personTable.getColumns().add(idColumn);
        personTable.getColumns().add(firstNameColumn);
        personTable.getColumns().add(lastNameColumn);
        personTable.getColumns().add(birthDateColumn);
        personTable.getColumns().add(genderColumn);
        personTable.getColumns().add(emailColumn);
        personTable.getColumns().add(phoneColumn);
        personTable.getColumns().add(usernameColumn);
    }

}
