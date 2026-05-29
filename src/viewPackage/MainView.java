package viewPackage;

import controllerPackage.AppointmentController;
import controllerPackage.GymMemberController;
import controllerPackage.PersonController;
import controllerPackage.SearchController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import modelPackage.GymMember;
import modelPackage.Person;
import modelPackage.Room;
import modelPackage.Speciality;
import modelPackage.searchResult.AppointmentSearchResult;
import modelPackage.searchResult.AvailableCoachSearchResult;
import modelPackage.searchResult.SponsoredMemberSearchResult;

import java.util.List;

public class MainView {

    private final BorderPane root;
    private GymMemberController gymMemberController;
    private PersonController personController;
    private SearchController searchController;
    private AppointmentController appointmentController;
    private Person connectedPerson;
    private AppointmentSearchView appointmentSearchView;
    private SponsoredMemberSearchView sponsoredMemberSearchView;
    private AvailableCoachSearchView availableCoachSearchView;
    private AppointmentBookingView appointmentBookingView;
    private Label infoBannerLabel;
    private InfoBannerThread infoBannerThread;

    public MainView() {
        root = new BorderPane();
        createInfoBanner();
        showLoginForm();
    }

    public Parent getRoot() {
        return root;
    }

    public void setGymMemberController(GymMemberController gymMemberController) {
        this.gymMemberController = gymMemberController;
    }

    public void setPersonController(PersonController personController) {
        this.personController = personController;
    }

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }

    public void setAppointmentController(AppointmentController appointmentController) {
        this.appointmentController = appointmentController;
    }

    public Person getConnectedPerson() {
        return connectedPerson;
    }

    public void login(Person person) {
        connectedPerson = person;
        root.setTop(createMenuBar());
        showWelcomeMessage();
    }

    public void logout() {
        connectedPerson = null;
        root.setTop(null);
        showLoginForm();
    }

    public void showLoginForm() {
        LoginView loginView = new LoginView();

        loginView.getLoginButton().setOnAction(event -> {
            try {
                if (personController != null) {
                    personController.login(loginView.getUsername(), loginView.getPassword());
                }
            } catch (Exception exception) {
                showErrorMessage(exception.getMessage());
            }
        });

        loginView.getCreateAccountButton().setOnAction(event -> {
            if (personController != null) {
                personController.showCreateAccountFormForLogin();
            }
        });

        root.setCenter(loginView.getRoot());
    }

    public void showPersonForm() {
        PersonFormView personFormView = new PersonFormView();

        personFormView.getCreateAccountButton().setOnAction(event -> {
            try {
                Person person = personFormView.createPerson();

                if (personController != null) {
                    personController.createPerson(person);
                }
            } catch (Exception exception) {
                showErrorMessage(exception.getMessage());
            }
        });

        personFormView.getCancelButton().setOnAction(event -> showWelcomeMessage());

        root.setCenter(personFormView.getRoot());
    }

    public void showPersonFormForLogin() {
        PersonFormView personFormView = new PersonFormView();

        personFormView.getCreateAccountButton().setOnAction(event -> {
            try {
                Person person = personFormView.createPerson();

                if (personController != null) {
                    personController.createPersonForLogin(person);
                }
            } catch (Exception exception) {
                showErrorMessage(exception.getMessage());
            }
        });

        personFormView.getCancelButton().setOnAction(event -> showLoginForm());

        root.setCenter(personFormView.getRoot());
    }

    public void showPersonList(List<Person> persons) {
        PersonListView personListView = new PersonListView(persons);

        personListView.getRefreshButton().setOnAction(event -> {
            if (personController != null) {
                personController.showPersons();
            }
        });

        root.setCenter(personListView.getRoot());
    }

    public void showGymMemberList(List<GymMember> members) {
        GymMemberListView gymMemberListView = new GymMemberListView(members);

        gymMemberListView.getAddButton().setOnAction(event -> {
            if (gymMemberController != null) {
                gymMemberController.showConnectedPersonRegistrationForm();
            }
        });

        gymMemberListView.getUpdateButton().setOnAction(event -> {
            if (gymMemberController != null) {
                gymMemberController.showUpdateMemberForm(gymMemberListView.getSelectedMember());
            }
        });

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

    public void showConnectedPersonGymMemberForm() {
        if (connectedPerson == null) {
            showErrorMessage("Veuillez d'abord vous connecter.");
            showLoginForm();
            return;
        }

        ConnectedPersonGymMemberFormView gymMemberFormView =
                new ConnectedPersonGymMemberFormView(connectedPerson);

        gymMemberFormView.getRegistrationButton().setOnAction(event -> {
            try {
                GymMember member = gymMemberFormView.createGymMember();

                if (gymMemberController != null) {
                    gymMemberController.addExistingAccountMember(member);
                }
            } catch (Exception exception) {
                showErrorMessage(exception.getMessage());
            }
        });

        gymMemberFormView.getCancelButton().setOnAction(event -> showWelcomeMessage());

        root.setCenter(gymMemberFormView.getRoot());
    }

    public void showGymMemberForm(GymMember member) {
        GymMemberFormView gymMemberFormView = new GymMemberFormView(member);

        gymMemberFormView.getRegistrationButton().setOnAction(event -> {
            try {
                GymMember formMember = gymMemberFormView.createGymMember();

                if (gymMemberController != null) {
                    if (gymMemberFormView.isUpdateMode()) {
                        gymMemberController.updateMember(formMember);
                    } else {
                        gymMemberController.addMember(formMember);
                    }
                }
            } catch (Exception exception) {
                showErrorMessage(exception.getMessage());
            }
        });

        gymMemberFormView.getCancelButton().setOnAction(event -> {
            if (gymMemberController != null) {
                gymMemberController.showMembers();
            } else {
                showWelcomeMessage();
            }
        });

        root.setCenter(gymMemberFormView.getRoot());
    }

    public void showAppointmentSearch(List<GymMember> members) {
        appointmentSearchView = new AppointmentSearchView(members);

        appointmentSearchView.getSearchButton().setOnAction(event -> {
            try {
                if (searchController != null) {
                    searchController.searchAppointments(
                            appointmentSearchView.getMemberId(),
                            appointmentSearchView.getStartDate(),
                            appointmentSearchView.getEndDate()
                    );
                }
            } catch (Exception exception) {
                showErrorMessage(exception.getMessage());
            }
        });

        root.setCenter(appointmentSearchView.getRoot());
    }

    public void showAppointmentSearchResults(List<AppointmentSearchResult> results) {
        if (appointmentSearchView != null) {
            appointmentSearchView.setResults(results);
        }
    }

    public void showSponsoredMemberSearch(List<GymMember> members) {
        sponsoredMemberSearchView = new SponsoredMemberSearchView(members);

        sponsoredMemberSearchView.getSearchButton().setOnAction(event -> {
            try {
                if (searchController != null) {
                    searchController.searchSponsoredMembers(
                            sponsoredMemberSearchView.getSponsorId()
                    );
                }
            } catch (Exception exception) {
                showErrorMessage(exception.getMessage());
            }
        });

        root.setCenter(sponsoredMemberSearchView.getRoot());
    }

    public void showSponsoredMemberSearchResults(List<SponsoredMemberSearchResult> results) {
        if (sponsoredMemberSearchView != null) {
            sponsoredMemberSearchView.setResults(results);
        }
    }

    public void showAvailableCoachSearch(List<Speciality> specialities) {
        availableCoachSearchView = new AvailableCoachSearchView(specialities);

        availableCoachSearchView.getSearchButton().setOnAction(event -> {
            try {
                if (searchController != null) {
                    searchController.searchAvailableCoaches(
                            availableCoachSearchView.getSpecialityName(),
                            availableCoachSearchView.getStartDate(),
                            availableCoachSearchView.getEndDate()
                    );
                }
            } catch (Exception exception) {
                showErrorMessage(exception.getMessage());
            }
        });

        root.setCenter(availableCoachSearchView.getRoot());
    }

    public void showAvailableCoachSearchResults(List<AvailableCoachSearchResult> results) {
        if (availableCoachSearchView != null) {
            availableCoachSearchView.setResults(results);
        }
    }

    public void showAppointmentBookingForm(List<Speciality> specialities, List<Room> rooms) {
        appointmentBookingView = new AppointmentBookingView(specialities, rooms);

        appointmentBookingView.getSearchButton().setOnAction(event -> {
            try {
                if (appointmentController != null) {
                    appointmentController.searchAvailableSlots(
                            appointmentBookingView.getSpecialityName(),
                            appointmentBookingView.getStartDate(),
                            appointmentBookingView.getEndDate()
                    );
                }
            } catch (Exception exception) {
                showErrorMessage(exception.getMessage());
            }
        });

        appointmentBookingView.getBookingButton().setOnAction(event -> {
            try {
                if (connectedPerson == null) {
                    showErrorMessage("Veuillez d'abord vous connecter.");
                    return;
                }

                if (appointmentController != null) {
                    appointmentController.bookAppointment(
                            connectedPerson.getId(),
                            appointmentBookingView.getSelectedAvailabilityId(),
                            appointmentBookingView.getSpecialityName(),
                            appointmentBookingView.getSelectedRoomId(),
                            appointmentBookingView.getObjective()
                    );
                }
            } catch (Exception exception) {
                showErrorMessage(exception.getMessage());
            }
        });

        appointmentBookingView.getCancelButton().setOnAction(event -> showWelcomeMessage());

        root.setCenter(appointmentBookingView.getRoot());
    }

    public void showAppointmentBookingSlots(List<AvailableCoachSearchResult> results) {
        if (appointmentBookingView != null) {
            appointmentBookingView.setAvailabilities(results);
        }
    }

    public void showWelcomeMessage() {
        String firstName = "";

        if (connectedPerson != null) {
            firstName = " " + connectedPerson.getFirstName();
        }

        Label titleLabel = new Label("Bienvenue" + firstName);
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label instructionLabel = new Label("Utilisez le menu pour acceder aux fonctionnalites.");

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

    private void createInfoBanner() {
        infoBannerLabel = new Label();
        infoBannerLabel.setPadding(new Insets(6, 12, 6, 12));
        infoBannerLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: white; -fx-background-color: #2b2b2b;");
        infoBannerLabel.setMaxWidth(Double.MAX_VALUE);
        infoBannerLabel.setMinHeight(30);

        root.setBottom(infoBannerLabel);

        infoBannerThread = new InfoBannerThread(infoBannerLabel);
        infoBannerThread.start();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu homeMenu = new Menu("Accueil");
        MenuItem welcomeItem = new MenuItem("Afficher l'accueil");
        welcomeItem.setOnAction(event -> showWelcomeMessage());

        MenuItem logoutItem = new MenuItem("Se deconnecter");
        logoutItem.setOnAction(event -> logout());

        homeMenu.getItems().add(welcomeItem);
        homeMenu.getItems().add(logoutItem);

        Menu accountMenu = new Menu("Comptes");

        MenuItem listAccountsItem = new MenuItem("Lister les comptes");
        listAccountsItem.setOnAction(event -> {
            if (personController != null) {
                personController.showPersons();
            }
        });

        accountMenu.getItems().add(listAccountsItem);

        Menu memberMenu = new Menu("Membres");

        MenuItem registrationItem = new MenuItem("Inscription");
        registrationItem.setOnAction(event -> {
            if (gymMemberController != null) {
                gymMemberController.showConnectedPersonRegistrationForm();
            }
        });

        MenuItem listMembersItem = new MenuItem("Lister les membres");
        listMembersItem.setOnAction(event -> {
            if (gymMemberController != null) {
                gymMemberController.showMembers();
            }
        });

        memberMenu.getItems().add(registrationItem);
        memberMenu.getItems().add(listMembersItem);

        Menu appointmentMenu = new Menu("Rendez-vous");

        MenuItem bookAppointmentItem = new MenuItem("Prendre rendez-vous");
        bookAppointmentItem.setOnAction(event -> {
            if (appointmentController != null) {
                appointmentController.showBookingForm();
            }
        });

        appointmentMenu.getItems().add(bookAppointmentItem);

        Menu searchMenu = new Menu("Recherches");

        MenuItem appointmentSearchItem = new MenuItem("Rendez-vous d'un membre");
        appointmentSearchItem.setOnAction(event -> {
            if (searchController != null) {
                searchController.showAppointmentSearch();
            }
        });

        MenuItem sponsoredMemberSearchItem = new MenuItem("Membres parraines");
        sponsoredMemberSearchItem.setOnAction(event -> {
            if (searchController != null) {
                searchController.showSponsoredMemberSearch();
            }
        });

        MenuItem availableCoachSearchItem = new MenuItem("Coachs disponibles");
        availableCoachSearchItem.setOnAction(event -> {
            if (searchController != null) {
                searchController.showAvailableCoachSearch();
            }
        });

        searchMenu.getItems().add(appointmentSearchItem);
        searchMenu.getItems().add(sponsoredMemberSearchItem);
        searchMenu.getItems().add(availableCoachSearchItem);

        menuBar.getMenus().addAll(homeMenu, accountMenu, memberMenu, appointmentMenu, searchMenu);

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
