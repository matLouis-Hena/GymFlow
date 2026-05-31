package viewPackage;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import modelPackage.CoachAvailability;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CoachAvailabilityManagementView {

    private final BorderPane root;
    private final TableView<CoachAvailability> availabilityTable;
    private TextField dateField;
    private TextField startTimeField;
    private TextField endTimeField;
    private Button addButton;
    private Button deleteButton;
    private Button refreshButton;

    public CoachAvailabilityManagementView(List<CoachAvailability> availabilities) {
        root = new BorderPane();
        availabilityTable = new TableView<>();

        createFields();
        createColumns();
        createView(availabilities);
    }

    public Parent getRoot() {
        return root;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    public CoachAvailability getSelectedAvailability() {
        return availabilityTable.getSelectionModel().getSelectedItem();
    }

    public LocalDate getDate() {
        return ViewInputHelper.getRequiredDate(
                dateField,
                "La date est obligatoire.",
                "La date doit réspecter le format yyyy-mm-dd."
        );
    }

    public LocalTime getStartTime() {
        return ViewInputHelper.getRequiredTime(
                startTimeField,
                "L'heure de début est obligatoire.",
                "L'heure doit réspecter le format hh:mm."
        );
    }

    public LocalTime getEndTime() {
        return ViewInputHelper.getRequiredTime(
                endTimeField,
                "L'heure de fin est obligatoire.",
                "L'heure doit réspecter le format hh:mm."
        );
    }

    private void createFields() {
        dateField = new TextField();
        startTimeField = new TextField();
        endTimeField = new TextField();
        addButton = new Button("Ajouter");
        deleteButton = new Button("Supprimer");
        refreshButton = new Button("Rafraîchir");

        dateField.setPromptText("yyyy-mm-dd");
        startTimeField.setPromptText("hh:mm");
        endTimeField.setPromptText("hh:mm");
    }

    private void createColumns() {
        TableColumn<CoachAvailability, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getId())
        );

        TableColumn<CoachAvailability, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getAvailableDate()))
        );

        TableColumn<CoachAvailability, String> startColumn = new TableColumn<>("Début");
        startColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getStartTime()))
        );

        TableColumn<CoachAvailability, String> endColumn = new TableColumn<>("Fin");
        endColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getEndTime()))
        );

        TableColumn<CoachAvailability, Boolean> bookedColumn = new TableColumn<>("Réservé");
        bookedColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().isBooked())
        );

        availabilityTable.getColumns().add(idColumn);
        availabilityTable.getColumns().add(dateColumn);
        availabilityTable.getColumns().add(startColumn);
        availabilityTable.getColumns().add(endColumn);
        availabilityTable.getColumns().add(bookedColumn);
    }

    private void createView(List<CoachAvailability> availabilities) {
        availabilityTable.setItems(FXCollections.observableArrayList(availabilities));
        availabilityTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        availabilityTable.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Label titleLabel = new Label("Mes disponibilités");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.CENTER);

        formGrid.add(new Label("Date"), 0, 0);
        formGrid.add(dateField, 1, 0);
        formGrid.add(new Label("Début"), 2, 0);
        formGrid.add(startTimeField, 3, 0);
        formGrid.add(new Label("Fin"), 4, 0);
        formGrid.add(endTimeField, 5, 0);

        HBox buttonBox = new HBox(10, addButton, deleteButton, refreshButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox container = new VBox(15, titleLabel, formGrid, availabilityTable, buttonBox);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(availabilityTable, Priority.ALWAYS);

        root.setCenter(container);
    }

}
