package testPackage;

import dataAccessPackage.coachAvailabilityDataAccess.CoachAvailabilityDBAccess;
import dataAccessPackage.coachAvailabilityDataAccess.ICoachAvailabilityDA;
import exceptionPackage.coachAvailability.ReadCoachAvailabilityException;
import exceptionPackage.coachAvailability.UpdateCoachAvailabilityException;
import modelPackage.CoachAvailability;

import java.time.LocalDate;
import java.util.List;

public class TestCoachAvailabilityDA {

    public static void main(String[] args) {
        ICoachAvailabilityDA coachAvailabilityDataAccess = new CoachAvailabilityDBAccess();

        try {
            List<CoachAvailability> availabilities = coachAvailabilityDataAccess.getAll();

            System.out.println("Disponibilités récupérées : " + availabilities.size());

            for (CoachAvailability availability : availabilities) {
                System.out.println(
                        availability.getId()
                                + " - "
                                + availability.getCoach().getFirstName()
                                + " "
                                + availability.getCoach().getLastName()
                                + " - "
                                + availability.getAvailableDate()
                                + " "
                                + availability.getStartTime()
                                + " à "
                                + availability.getEndTime()
                                + " - réservé : "
                                + availability.isBooked()
                );
            }

            if (!availabilities.isEmpty()) {
                CoachAvailability availability = coachAvailabilityDataAccess.getById(
                        availabilities.get(0).getId()
                );

                System.out.println(
                        "Disponibilité récupérée : "
                                + availability.getId()
                                + " - coach : "
                                + availability.getCoach().getFirstName()
                                + " "
                                + availability.getCoach().getLastName()
                );

                coachAvailabilityDataAccess.markAsBooked(availability.getId());
                System.out.println("Disponibilité marquée comme réservée.");

                coachAvailabilityDataAccess.markAsAvailable(availability.getId());
                System.out.println("Disponibilité remise comme disponible.");
            }

            List<CoachAvailability> musculationAvailabilities =
                    coachAvailabilityDataAccess.getAvailableBySpecialityAndDateRange(
                            "Musculation",
                            LocalDate.now(),
                            LocalDate.now().plusMonths(1)
                    );

            System.out.println(
                    "Disponibilités Musculation sur 1 mois : "
                            + musculationAvailabilities.size()
            );

        } catch (ReadCoachAvailabilityException | UpdateCoachAvailabilityException exception) {
            System.out.println("Erreur pendant le test CoachAvailabilityDA : " + exception.getMessage());
        }
    }
}