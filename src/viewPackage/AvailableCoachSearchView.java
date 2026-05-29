package viewPackage;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import modelPackage.Speciality;
import modelPackage.searchResult.AvailableCoachSearchResult;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AvailableCoachSearchView {

    private final BorderPane root;
    private final ComboBox<String> specialityComboBox;
    private final TextField startDateField;
    private final TextField endDateField;
    private final Button searchButton;
    private final TableView<AvailableCoachSearchResult> resultTable;

    public AvailableCoachSearchView(List<Speciality> specialities) {
        root = new BorderPane();
        specialityComboBox = new ComboBox<>();
        startDateField = new TextField();
        endDateField = new TextField();
        searchButton = new Button("Rechercher");
        resultTable = new TableView<>();

        fillSpecialities(specialities);
        createColumns();
        createContent();
    }

    public Parent getRoot() {
        return root;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public String getSpecialityName() {
        String value = specialityComboBox.getSelectionModel().getSelectedItem();

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("La specialite est obligatoire.");
        }

        return value;
    }

    public LocalDate getStartDate() {
        return getDate(startDateField, "La date de debut est obligatoire.");
    }

    public LocalDate getEndDate() {
        return getDate(endDateField, "La date de fin est obligatoire.");
    }

    public void setResults(List<AvailableCoachSearchResult> results) {
        resultTable.setItems(FXCollections.observableArrayList(results));
    }

    private void createContent() {
        Label titleLabel = new Label("Coachs disponibles");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane criteriaGrid = new GridPane();
        criteriaGrid.setHgap(15);
        criteriaGrid.setVgap(10);
        criteriaGrid.setAlignment(Pos.CENTER);

        criteriaGrid.add(new Label("Specialite"), 0, 0);
        criteriaGrid.add(specialityComboBox, 1, 0);
        criteriaGrid.add(new Label("Date debut (yyyy-mm-dd)"), 0, 1);
        criteriaGrid.add(startDateField, 1, 1);
        criteriaGrid.add(new Label("Date fin (yyyy-mm-dd)"), 0, 2);
        criteriaGrid.add(endDateField, 1, 2);
        criteriaGrid.add(searchButton, 1, 3);

        VBox container = new VBox(15, titleLabel, criteriaGrid, resultTable);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);
        resultTable.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(resultTable, Priority.ALWAYS);

        root.setCenter(container);
    }

    private void createColumns() {
        TableColumn<AvailableCoachSearchResult, Integer> idColumn = new TableColumn<>("Creneau");
        idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getAvailabilityId())
        );

        TableColumn<AvailableCoachSearchResult, String> coachColumn = new TableColumn<>("Coach");
        coachColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getCoachFirstName()
                                + " "
                                + cellData.getValue().getCoachLastName()
                )
        );

        TableColumn<AvailableCoachSearchResult, Boolean> degreeColumn = new TableColumn<>("Diplome");
        degreeColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getHasDegree())
        );

        TableColumn<AvailableCoachSearchResult, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getAvailableDate()))
        );

        TableColumn<AvailableCoachSearchResult, String> timeColumn = new TableColumn<>("Heure");
        timeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getStartTime()
                                + " - "
                                + cellData.getValue().getEndTime()
                )
        );

        resultTable.getColumns().add(idColumn);
        resultTable.getColumns().add(coachColumn);
        resultTable.getColumns().add(degreeColumn);
        resultTable.getColumns().add(dateColumn);
        resultTable.getColumns().add(timeColumn);
        resultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void fillSpecialities(List<Speciality> specialities) {
        for (Speciality speciality : specialities) {
            specialityComboBox.getItems().add(speciality.getName());
        }
    }

    private LocalDate getDate(TextField textField, String message) {
        String value = textField.getText();

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }

        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("La date doit respecter le format yyyy-mm-dd.");
        }
    }
}
