package controllerPackage;

import businessPackage.CoachAvailabilityManager;
import exceptionPackage.coachAvailability.ReadCoachAvailabilityException;
import exceptionPackage.coachAvailability.UpdateCoachAvailabilityException;
import modelPackage.CoachAvailability;
import viewPackage.MainView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CoachAvailabilityController {

    private final CoachAvailabilityManager coachAvailabilityManager;
    private final MainView mainView;

    public CoachAvailabilityController(
            CoachAvailabilityManager coachAvailabilityManager,
            MainView mainView
    ) {
        this.coachAvailabilityManager = coachAvailabilityManager;
        this.mainView = mainView;
    }

    public void showMyAvailabilities() {
        try {
            int coachId = mainView.getConnectedPerson().getId();
            List<CoachAvailability> availabilities =
                    coachAvailabilityManager.getAvailabilitiesByCoachId(coachId);

            mainView.showCoachAvailabilityManagement(availabilities);

        } catch (ReadCoachAvailabilityException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void addAvailability(LocalDate date, LocalTime startTime, LocalTime endTime) {
        try {
            int coachId = mainView.getConnectedPerson().getId();
            coachAvailabilityManager.addAvailability(coachId, date, startTime, endTime);
            mainView.showInformationMessage("Disponibilite ajoutee avec succes.");
            showMyAvailabilities();

        } catch (UpdateCoachAvailabilityException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void deleteAvailability(CoachAvailability availability) {
        if (availability == null) {
            mainView.showErrorMessage("Veuillez selectionner une disponibilite.");
            return;
        }

        try {
            coachAvailabilityManager.deleteAvailability(availability.getId());
            mainView.showInformationMessage("Disponibilite supprimee avec succes.");
            showMyAvailabilities();

        } catch (UpdateCoachAvailabilityException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }
}
