package viewPackage;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import modelPackage.Room;
import modelPackage.Speciality;
import modelPackage.searchResult.AvailableCoachSearchResult;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AppointmentBookingView {

    private final BorderPane root;
    private final ComboBox<String> specialityComboBox;
    private final TextField startDateField;
    private final TextField endDateField;
    private final Button searchButton;
    private final TableView<AvailableCoachSearchResult> availabilityTable;
    private final ComboBox<String> roomComboBox;
    private final TextField objectiveField;
    private final Button bookingButton;
    private final Button cancelButton;

    public AppointmentBookingView(List<Speciality> specialities, List<Room> rooms) {
        root = new BorderPane();
        specialityComboBox = new ComboBox<>();
        startDateField = new TextField();
        endDateField = new TextField();
        searchButton = new Button("Rechercher les créneaux");
        availabilityTable = new TableView<>();
        roomComboBox = new ComboBox<>();
        objectiveField = new TextField();
        bookingButton = new Button("Réserver");
        cancelButton = new Button("Annuler");

        fillSpecialities(specialities);
        fillRooms(rooms);
        createColumns();
        createContent();
    }

    public Parent getRoot() {
        return root;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public Button getBookingButton() {
        return bookingButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public String getSpecialityName() {
        String value = specialityComboBox.getSelectionModel().getSelectedItem();

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("La spécialité est obligatoire.");
        }

        return value;
    }

    public LocalDate getStartDate() {
        return getDate(startDateField, "La date de début est obligatoire.");
    }

    public LocalDate getEndDate() {
        return getDate(endDateField, "La date de fin est obligatoire.");
    }

    public int getSelectedAvailabilityId() {
        AvailableCoachSearchResult result = availabilityTable.getSelectionModel().getSelectedItem();

        if (result == null) {
            throw new IllegalArgumentException("Veuillez sélectionner un créneau.");
        }

        return result.getAvailabilityId();
    }

    public Integer getSelectedRoomId() {
        String value = roomComboBox.getSelectionModel().getSelectedItem();

        if (value == null || value.startsWith("0 - ")) {
            return null;
        }

        return Integer.parseInt(value.substring(0, value.indexOf(" - ")));
    }

    public String getObjective() {
        String value = objectiveField.getText();

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return value.trim();
    }

    public void setAvailabilities(List<AvailableCoachSearchResult> availabilities) {
        availabilityTable.setItems(FXCollections.observableArrayList(availabilities));
    }

    private void createContent() {
        Label titleLabel = new Label("Prendre rendez-vous");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane criteriaGrid = new GridPane();
        criteriaGrid.setHgap(15);
        criteriaGrid.setVgap(10);
        criteriaGrid.setAlignment(Pos.CENTER);

        criteriaGrid.add(new Label("Spécialité"), 0, 0);
        criteriaGrid.add(specialityComboBox, 1, 0);
        criteriaGrid.add(new Label("Date début (yyyy-mm-dd)"), 0, 1);
        criteriaGrid.add(startDateField, 1, 1);
        criteriaGrid.add(new Label("Date fin (yyyy-mm-dd)"), 0, 2);
        criteriaGrid.add(endDateField, 1, 2);
        criteriaGrid.add(searchButton, 1, 3);

        GridPane bookingGrid = new GridPane();
        bookingGrid.setHgap(15);
        bookingGrid.setVgap(10);
        bookingGrid.setAlignment(Pos.CENTER);

        bookingGrid.add(new Label("Salle"), 0, 0);
        bookingGrid.add(roomComboBox, 1, 0);
        bookingGrid.add(new Label("Objectif"), 0, 1);
        bookingGrid.add(objectiveField, 1, 1);

        HBox buttonBox = new HBox(10, bookingButton, cancelButton);
        bookingGrid.add(buttonBox, 1, 2);

        VBox container = new VBox(15, titleLabel, criteriaGrid, availabilityTable, bookingGrid);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);
        availabilityTable.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(availabilityTable, Priority.ALWAYS);

        root.setCenter(container);
    }

    private void createColumns() {
        TableColumn<AvailableCoachSearchResult, Integer> idColumn = new TableColumn<>("Créneau");
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

        availabilityTable.getColumns().add(idColumn);
        availabilityTable.getColumns().add(coachColumn);
        availabilityTable.getColumns().add(dateColumn);
        availabilityTable.getColumns().add(timeColumn);
        availabilityTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void fillSpecialities(List<Speciality> specialities) {
        for (Speciality speciality : specialities) {
            specialityComboBox.getItems().add(speciality.getName());
        }
    }

    private void fillRooms(List<Room> rooms) {
        roomComboBox.getItems().add("0 - Aucune salle");

        for (Room room : rooms) {
            roomComboBox.getItems().add(
                    room.getId()
                            + " - "
                            + room.getName()
            );
        }

        roomComboBox.getSelectionModel().selectFirst();
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
