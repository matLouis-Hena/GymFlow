package viewPackage;

import businessPackage.GymMemberManager;
import controllerPackage.GymMemberController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        GymMemberManager gymMemberManager = new GymMemberManager();

        MainView mainView = new MainView();
        GymMemberController gymMemberController = new GymMemberController(gymMemberManager, mainView);

        mainView.setGymMemberController(gymMemberController);

        Scene scene = new Scene(mainView.getRoot(), 1000, 650);

        primaryStage.setTitle("Gestion salle de sport");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}