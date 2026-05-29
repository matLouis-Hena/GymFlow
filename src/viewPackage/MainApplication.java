package viewPackage;

import businessPackage.AppointmentManager;
import businessPackage.GymMemberManager;
import businessPackage.PersonManager;
import businessPackage.RoomManager;
import businessPackage.SearchManager;
import businessPackage.SpecialityManager;
import controllerPackage.AppointmentController;
import controllerPackage.GymMemberController;
import controllerPackage.PersonController;
import controllerPackage.SearchController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        GymMemberManager gymMemberManager = new GymMemberManager();
        PersonManager personManager = new PersonManager();
        SearchManager searchManager = new SearchManager();
        SpecialityManager specialityManager = new SpecialityManager();
        AppointmentManager appointmentManager = new AppointmentManager();
        RoomManager roomManager = new RoomManager();

        MainView mainView = new MainView();
        GymMemberController gymMemberController = new GymMemberController(gymMemberManager, mainView);
        PersonController personController = new PersonController(personManager, mainView);
        SearchController searchController = new SearchController(
                searchManager,
                gymMemberManager,
                specialityManager,
                mainView
        );
        AppointmentController appointmentController = new AppointmentController(
                appointmentManager,
                searchManager,
                specialityManager,
                roomManager,
                mainView
        );

        mainView.setGymMemberController(gymMemberController);
        mainView.setPersonController(personController);
        mainView.setSearchController(searchController);
        mainView.setAppointmentController(appointmentController);

        Scene scene = new Scene(mainView.getRoot(), 1000, 650);

        primaryStage.setTitle("Gestion salle de sport");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
