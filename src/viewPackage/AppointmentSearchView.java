package viewPackage;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import modelPackage.GymMember;
import modelPackage.searchResult.AppointmentSearchResult;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AppointmentSearchView {

    private final BorderPane root;
    private final ComboBox<String> memberComboBox;
    private final TextField startDateField;
    private final TextField endDateField;
    private final Button searchButton;
    private final TableView<AppointmentSearchResult> resultTable;

    public AppointmentSearchView(List<GymMember> members) {
        root = new BorderPane();
        memberComboBox = new ComboBox<>();
        startDateField = new TextField();
        endDateField = new TextField();
        searchButton = new Button("Rechercher");
        resultTable = new TableView<>();

        fillMembers(members);
        createColumns();
        createContent();
    }

    public Parent getRoot() {
        return root;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public int getMemberId() {
        return getSelectedId(memberComboBox, "Le membre est obligatoire.");
    }

    public LocalDate getStartDate() {
        return getDate(startDateField, "La date de debut est obligatoire.");
    }

    public LocalDate getEndDate() {
        return getDate(endDateField, "La date de fin est obligatoire.");
    }

    public void setResults(List<AppointmentSearchResult> results) {
        resultTable.setItems(FXCollections.observableArrayList(results));
    }

    private void createContent() {
        Label titleLabel = new Label("Rendez-vous d'un membre");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane criteriaGrid = new GridPane();
        criteriaGrid.setHgap(15);
        criteriaGrid.setVgap(10);
        criteriaGrid.setAlignment(Pos.CENTER);

        criteriaGrid.add(new Label("Membre"), 0, 0);
        criteriaGrid.add(memberComboBox, 1, 0);
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
        TableColumn<AppointmentSearchResult, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getAppointmentId())
        );

        TableColumn<AppointmentSearchResult, String> coachColumn = new TableColumn<>("Coach");
        coachColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getCoachFirstName()
                                + " "
                                + cellData.getValue().getCoachLastName()
                )
        );

        TableColumn<AppointmentSearchResult, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getAvailableDate()))
        );

        TableColumn<AppointmentSearchResult, String> timeColumn = new TableColumn<>("Heure");
        timeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getStartTime()
                                + " - "
                                + cellData.getValue().getEndTime()
                )
        );

        TableColumn<AppointmentSearchResult, String> objectiveColumn = new TableColumn<>("Objectif");
        objectiveColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(getNullableText(cellData.getValue().getObjective()))
        );

        TableColumn<AppointmentSearchResult, String> statusColumn = new TableColumn<>("Statut");
        statusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getStatus()))
        );

        TableColumn<AppointmentSearchResult, String> roomColumn = new TableColumn<>("Salle");
        roomColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(getNullableText(cellData.getValue().getRoomName()))
        );

        resultTable.getColumns().add(idColumn);
        resultTable.getColumns().add(coachColumn);
        resultTable.getColumns().add(dateColumn);
        resultTable.getColumns().add(timeColumn);
        resultTable.getColumns().add(objectiveColumn);
        resultTable.getColumns().add(statusColumn);
        resultTable.getColumns().add(roomColumn);
        resultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void fillMembers(List<GymMember> members) {
        for (GymMember member : members) {
            memberComboBox.getItems().add(
                    member.getId()
                            + " - "
                            + member.getFirstName()
                            + " "
                            + member.getLastName()
            );
        }
    }

    private int getSelectedId(ComboBox<String> comboBox, String message) {
        String selectedValue = comboBox.getSelectionModel().getSelectedItem();

        if (selectedValue == null || selectedValue.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return Integer.parseInt(selectedValue.substring(0, selectedValue.indexOf(" - ")));
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

    private String getNullableText(Object value) {
        if (value == null) {
            return "";
        }

        return String.valueOf(value);
    }
}
