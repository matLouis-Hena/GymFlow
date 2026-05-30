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
import modelPackage.GymMember;
import modelPackage.searchResult.SponsoredMemberSearchResult;

import java.util.List;

public class SponsoredMemberSearchView {

    private final BorderPane root;
    private final ComboBox<String> sponsorComboBox;
    private final Button searchButton;
    private final TableView<SponsoredMemberSearchResult> resultTable;

    public SponsoredMemberSearchView(List<GymMember> members) {
        root = new BorderPane();
        sponsorComboBox = new ComboBox<>();
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

    public int getSponsorId() {
        return getSelectedId(sponsorComboBox, "Le parrain est obligatoire.");
    }

    public void setResults(List<SponsoredMemberSearchResult> results) {
        resultTable.setItems(FXCollections.observableArrayList(results));
    }

    private void createContent() {
        Label titleLabel = new Label("Membres parraines");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane criteriaGrid = new GridPane();
        criteriaGrid.setHgap(15);
        criteriaGrid.setVgap(10);
        criteriaGrid.setAlignment(Pos.CENTER);

        criteriaGrid.add(new Label("Parrain"), 0, 0);
        criteriaGrid.add(sponsorComboBox, 1, 0);
        criteriaGrid.add(searchButton, 1, 1);

        VBox container = new VBox(15, titleLabel, criteriaGrid, resultTable);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.TOP_CENTER);
        resultTable.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox.setVgrow(resultTable, Priority.ALWAYS);

        root.setCenter(container);
    }

    private void createColumns() {
        TableColumn<SponsoredMemberSearchResult, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getSponsoredMemberId())
        );

        TableColumn<SponsoredMemberSearchResult, String> nameColumn = new TableColumn<>("Membre");
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getFirstName()
                                + " "
                                + cellData.getValue().getLastName()
                )
        );

        TableColumn<SponsoredMemberSearchResult, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEmail())
        );

        TableColumn<SponsoredMemberSearchResult, Boolean> wantsLockerColumn = new TableColumn<>("Veut casier");
        wantsLockerColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getWantsLocker())
        );

        TableColumn<SponsoredMemberSearchResult, Integer> lockerNumberColumn = new TableColumn<>("Casier");
        lockerNumberColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getLockerNumber())
        );

        TableColumn<SponsoredMemberSearchResult, Double> weightColumn = new TableColumn<>("Poids");
        weightColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getWeight())
        );

        TableColumn<SponsoredMemberSearchResult, Integer> heightColumn = new TableColumn<>("Taille");
        heightColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getHeight())
        );

        TableColumn<SponsoredMemberSearchResult, String> subscriptionColumn = new TableColumn<>("Abonnement");
        subscriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getSubscriptionType()))
        );

        TableColumn<SponsoredMemberSearchResult, Double> priceColumn = new TableColumn<>("Prix");
        priceColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getSubscriptionPrice())
        );

        TableColumn<SponsoredMemberSearchResult, Integer> durationColumn = new TableColumn<>("Durée");
        durationColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getSubscriptionDurationMonths())
        );

        resultTable.getColumns().add(idColumn);
        resultTable.getColumns().add(nameColumn);
        resultTable.getColumns().add(emailColumn);
        resultTable.getColumns().add(wantsLockerColumn);
        resultTable.getColumns().add(lockerNumberColumn);
        resultTable.getColumns().add(weightColumn);
        resultTable.getColumns().add(heightColumn);
        resultTable.getColumns().add(subscriptionColumn);
        resultTable.getColumns().add(priceColumn);
        resultTable.getColumns().add(durationColumn);
        resultTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void fillMembers(List<GymMember> members) {
        for (GymMember member : members) {
            sponsorComboBox.getItems().add(
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
}
