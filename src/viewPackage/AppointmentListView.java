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
import modelPackage.Appointment;

import java.util.List;

public class AppointmentListView {

    private final BorderPane root;
    private final TableView<Appointment> appointmentTable;
    private final Button refreshButton;

    public AppointmentListView(List<Appointment> appointments) {
        root = new BorderPane();
        appointmentTable = new TableView<>();
        refreshButton = new Button("Rafraichir");

        createColumns();
        createView(appointments);
    }

    public Parent getRoot() {
        return root;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }

    private void createColumns() {
        TableColumn<Appointment, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getId())
        );

        TableColumn<Appointment, String> memberColumn = new TableColumn<>("Membre");
        memberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMember().getUsername())
        );

        TableColumn<Appointment, String> coachColumn = new TableColumn<>("Coach");
        coachColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAvailability().getCoach().getUsername())
        );

        TableColumn<Appointment, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getAvailability().getAvailableDate()))
        );

        TableColumn<Appointment, String> startColumn = new TableColumn<>("Debut");
        startColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getAvailability().getStartTime()))
        );

        TableColumn<Appointment, String> endColumn = new TableColumn<>("Fin");
        endColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getAvailability().getEndTime()))
        );

        TableColumn<Appointment, String> roomColumn = new TableColumn<>("Salle");
        roomColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(getRoomName(cellData.getValue()))
        );

        TableColumn<Appointment, String> objectiveColumn = new TableColumn<>("Objectif");
        objectiveColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(getObjective(cellData.getValue()))
        );

        TableColumn<Appointment, String> statusColumn = new TableColumn<>("Statut");
        statusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getStatus()))
        );

        TableColumn<Appointment, String> cancellationReasonColumn = new TableColumn<>("Motif annulation");
        cancellationReasonColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(getCancellationReason(cellData.getValue()))
        );

        appointmentTable.getColumns().add(idColumn);
        appointmentTable.getColumns().add(memberColumn);
        appointmentTable.getColumns().add(coachColumn);
        appointmentTable.getColumns().add(dateColumn);
        appointmentTable.getColumns().add(startColumn);
        appointmentTable.getColumns().add(endColumn);
        appointmentTable.getColumns().add(roomColumn);
        appointmentTable.getColumns().add(objectiveColumn);
        appointmentTable.getColumns().add(statusColumn);
        appointmentTable.getColumns().add(cancellationReasonColumn);
    }

    private void createView(List<Appointment> appointments) {
        appointmentTable.setItems(FXCollections.observableArrayList(appointments));
        appointmentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        appointmentTable.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Label titleLabel = new Label("Liste des rendez-vous");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        HBox buttonBox = new HBox(10, refreshButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox container = new VBox(15, titleLabel, appointmentTable, buttonBox);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);
        VBox.setVgrow(appointmentTable, Priority.ALWAYS);

        root.setCenter(container);
    }

    private String getRoomName(Appointment appointment) {
        if (appointment.getRoom() == null) {
            return "";
        }

        return appointment.getRoom().getName();
    }

    private String getObjective(Appointment appointment) {
        if (appointment.getObjective() == null) {
            return "";
        }

        return appointment.getObjective();
    }

    private String getCancellationReason(Appointment appointment) {
        if (appointment.getCancellationReason() == null) {
            return "";
        }

        return appointment.getCancellationReason();
    }
}