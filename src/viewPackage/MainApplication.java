package viewPackage;

import businessPackage.GymMemberManager;
import businessPackage.PersonManager;
import controllerPackage.GymMemberController;
import controllerPackage.PersonController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        GymMemberManager gymMemberManager = new GymMemberManager();
        PersonManager personManager = new PersonManager();

        MainView mainView = new MainView();
        GymMemberController gymMemberController = new GymMemberController(gymMemberManager, mainView);
        PersonController personController = new PersonController(personManager, mainView);

        mainView.setGymMemberController(gymMemberController);
        mainView.setPersonController(personController);

        Scene scene = new Scene(mainView.getRoot(), 1000, 650);

        primaryStage.setTitle("Gestion salle de sport");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
