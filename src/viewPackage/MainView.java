package viewPackage;

import controllerPackage.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import modelPackage.*;
import modelPackage.searchResult.*;

import java.util.List;

public class MainView {

    private final BorderPane root;
    private GymMemberController gymMemberController;
    private PersonController personController;
    private SearchController searchController;
    private AppointmentController appointmentController;
    private CoachAvailabilityController coachAvailabilityController;
    private Person connectedPerson;
    private UserRole connectedUserRole;
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

    public void setCoachAvailabilityController(CoachAvailabilityController coachAvailabilityController) {
        this.coachAvailabilityController = coachAvailabilityController;
    }

    public Person getConnectedPerson() {
        return connectedPerson;
    }

    public UserRole getConnectedUserRole() {
        return connectedUserRole;
    }

    public void login(Person person, UserRole userRole) {
        connectedPerson = person;
        connectedUserRole = userRole;
        root.setTop(createMenuBar());
        showWelcomeMessage();
    }

    public void logout() {
        connectedPerson = null;
        connectedUserRole = null;
        root.setTop(null);
        showLoginForm();
    }

    public void setConnectedUserRole(UserRole userRole) {
        connectedUserRole = userRole;
        root.setTop(createMenuBar());
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
            if (connectedUserRole == UserRole.ADMIN) {
                showGymMemberForm(null);
            } else if (gymMemberController != null) {
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
                    gymMemberController.addExistingAccountMember(
                            member,
                            gymMemberFormView.getSponsorUsername()
                    );
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
                        gymMemberController.addMember(
                                formMember,
                                gymMemberFormView.getSponsorUsername()
                        );
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

    public void showMemberAccount(GymMember member, String sponsorText) {
        MemberAccountView memberAccountView = new MemberAccountView(member, sponsorText);

        memberAccountView.getSaveButton().setOnAction(event -> {
            try {
                if (gymMemberController != null) {
                    gymMemberController.updateMyAccount(
                            memberAccountView.createUpdatedMember()
                    );
                }
            } catch (Exception exception) {
                showErrorMessage(exception.getMessage());
            }
        });

        memberAccountView.getBackButton().setOnAction(event -> showWelcomeMessage());

        root.setCenter(memberAccountView.getRoot());
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

    public void showAppointmentList(List<modelPackage.Appointment> appointments) {
        AppointmentListView appointmentListView = new AppointmentListView(appointments);
        appointmentListView.getConfirmButton().setVisible(connectedUserRole == UserRole.COACH);
        appointmentListView.getConfirmButton().setManaged(connectedUserRole == UserRole.COACH);
        appointmentListView.getCancelButton().setVisible(
                connectedUserRole == UserRole.MEMBER_WITH_SUBSCRIPTION
                        || connectedUserRole == UserRole.COACH
                        || connectedUserRole == UserRole.ADMIN
        );
        appointmentListView.getCancelButton().setManaged(appointmentListView.getCancelButton().isVisible());

        appointmentListView.getRefreshButton().setOnAction(event -> {
            refreshAppointmentList();
        });

        appointmentListView.getConfirmButton().setOnAction(event -> {
            if (appointmentController != null) {
                appointmentController.confirmAppointment(appointmentListView.getSelectedAppointment());
            }
        });

        appointmentListView.getCancelButton().setOnAction(event -> {
            if (appointmentController == null) {
                return;
            }

            String reason = askText("Motif d'annulation", "Motif");

            if (reason != null) {
                appointmentController.cancelAppointment(appointmentListView.getSelectedAppointment(), reason);
            }
        });

        root.setCenter(appointmentListView.getRoot());
    }

    public void refreshAppointmentList() {
        if (appointmentController == null || connectedPerson == null) {
            return;
        }

        if (connectedUserRole == UserRole.MEMBER_WITH_SUBSCRIPTION) {
            appointmentController.showAppointmentsForMember(connectedPerson.getId());
        } else if (connectedUserRole == UserRole.COACH) {
            appointmentController.showAppointmentsForCoach(connectedPerson.getId());
        } else if (connectedUserRole == UserRole.ADMIN) {
            appointmentController.showAppointmentList();
        }
    }

    public void showCoachAvailabilityManagement(List<CoachAvailability> availabilities) {
        CoachAvailabilityManagementView coachAvailabilityManagementView =
                new CoachAvailabilityManagementView(availabilities);

        coachAvailabilityManagementView.getAddButton().setOnAction(event -> {
            try {
                if (coachAvailabilityController != null) {
                    coachAvailabilityController.addAvailability(
                            coachAvailabilityManagementView.getDate(),
                            coachAvailabilityManagementView.getStartTime(),
                            coachAvailabilityManagementView.getEndTime()
                    );
                }
            } catch (Exception exception) {
                showErrorMessage(exception.getMessage());
            }
        });

        coachAvailabilityManagementView.getDeleteButton().setOnAction(event -> {
            if (coachAvailabilityController != null) {
                coachAvailabilityController.deleteAvailability(
                        coachAvailabilityManagementView.getSelectedAvailability()
                );
            }
        });

        coachAvailabilityManagementView.getRefreshButton().setOnAction(event -> {
            if (coachAvailabilityController != null) {
                coachAvailabilityController.showMyAvailabilities();
            }
        });

        root.setCenter(coachAvailabilityManagementView.getRoot());
    }

    public void showWelcomeMessage() {
        String firstName = "";

        if (connectedPerson != null) {
            firstName = " " + connectedPerson.getFirstName();
        }

        Label titleLabel = new Label("Bienvenue" + firstName);
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        String roleText = "";

        if (connectedUserRole != null) {
            roleText = "Role : " + connectedUserRole.getDisplayName();
        }

        Label roleLabel = new Label(roleText);
        Label instructionLabel = new Label("Utilisez le menu pour acceder aux fonctionnalites.");

        VBox welcomeBox = new VBox(15, titleLabel, roleLabel, instructionLabel);
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

        menuBar.getMenus().add(homeMenu);

        if (connectedUserRole == UserRole.ADMIN) {
            menuBar.getMenus().add(createAccountMenu());
            menuBar.getMenus().add(createMemberMenu(false, true));
            menuBar.getMenus().add(createAdminAppointmentMenu());
            menuBar.getMenus().add(createSearchMenu());
        } else if (connectedUserRole == UserRole.COACH) {
            menuBar.getMenus().add(createCoachMenu());
        } else if (connectedUserRole == UserRole.MEMBER_WITH_SUBSCRIPTION) {
            menuBar.getMenus().add(createMyAccountMenu());
            menuBar.getMenus().add(createAppointmentMenu());
        } else {
            menuBar.getMenus().add(createMemberMenu(true, false));
        }

        return menuBar;
    }

    private Menu createAccountMenu() {
        Menu accountMenu = new Menu("Comptes");

        MenuItem listAccountsItem = new MenuItem("Lister les comptes");
        listAccountsItem.setOnAction(event -> {
            if (personController != null) {
                personController.showPersons();
            }
        });

        accountMenu.getItems().add(listAccountsItem);

        return accountMenu;
    }

    private Menu createMemberMenu(boolean showRegistration, boolean showList) {
        Menu memberMenu = new Menu("Membres");

        if (showRegistration) {
            MenuItem registrationItem = new MenuItem("S'inscrire a la salle");
            registrationItem.setOnAction(event -> {
                if (gymMemberController != null) {
                    gymMemberController.showConnectedPersonRegistrationForm();
                }
            });

            memberMenu.getItems().add(registrationItem);
        }

        if (showList) {
            MenuItem listMembersItem = new MenuItem("Lister les membres");
            listMembersItem.setOnAction(event -> {
                if (gymMemberController != null) {
                    gymMemberController.showMembers();
                }
            });

            memberMenu.getItems().add(listMembersItem);
        }

        return memberMenu;
    }

    private Menu createAppointmentMenu() {
        Menu appointmentMenu = new Menu("Rendez-vous");

        MenuItem bookAppointmentItem = new MenuItem("Prendre rendez-vous");
        bookAppointmentItem.setOnAction(event -> {
            if (appointmentController != null) {
                appointmentController.showBookingForm();
            }
        });

        MenuItem myAppointmentsItem = new MenuItem("Mes rendez-vous");
        myAppointmentsItem.setOnAction(event -> {
            if (appointmentController != null && connectedPerson != null) {
                appointmentController.showAppointmentsForMember(connectedPerson.getId());
            }
        });

        appointmentMenu.getItems().add(bookAppointmentItem);
        appointmentMenu.getItems().add(myAppointmentsItem);

        return appointmentMenu;
    }

    private Menu createAdminAppointmentMenu() {
        Menu appointmentMenu = new Menu("Rendez-vous");

        MenuItem listAppointmentItem = new MenuItem("Lister les rendez-vous");
        listAppointmentItem.setOnAction(event -> {
            if (appointmentController != null) {
                appointmentController.showAppointmentList();
            }
        });

        appointmentMenu.getItems().add(listAppointmentItem);

        return appointmentMenu;
    }

    private Menu createMyAccountMenu() {
        Menu myAccountMenu = new Menu("Mon compte");

        MenuItem myAccountItem = new MenuItem("Voir mon compte");
        myAccountItem.setOnAction(event -> {
            if (gymMemberController != null) {
                gymMemberController.showMyAccount();
            }
        });

        myAccountMenu.getItems().add(myAccountItem);

        return myAccountMenu;
    }

    private Menu createCoachMenu() {
        Menu coachMenu = new Menu("Coach");

        MenuItem myAvailabilitiesItem = new MenuItem("Mes disponibilites");
        myAvailabilitiesItem.setOnAction(event -> {
            if (coachAvailabilityController != null) {
                coachAvailabilityController.showMyAvailabilities();
            }
        });

        MenuItem myAppointmentsItem = new MenuItem("Mes rendez-vous");
        myAppointmentsItem.setOnAction(event -> {
            if (appointmentController != null && connectedPerson != null) {
                appointmentController.showAppointmentsForCoach(connectedPerson.getId());
            }
        });

        coachMenu.getItems().add(myAvailabilitiesItem);
        coachMenu.getItems().add(myAppointmentsItem);

        return coachMenu;
    }

    private Menu createSearchMenu() {
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

        return searchMenu;
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

    public String askText(String title, String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);

        return dialog.showAndWait().orElse(null);
    }
}
