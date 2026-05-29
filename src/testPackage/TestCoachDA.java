package testPackage;

import dataAccessPackage.coachDataAccess.CoachDBAccess;
import dataAccessPackage.coachDataAccess.ICoachDA;
import exceptionPackage.coach.ReadCoachException;
import modelPackage.Coach;

import java.util.List;

public class TestCoachDA {

    public static void main(String[] args) {
        ICoachDA coachDataAccess = new CoachDBAccess();

        try {
            List<Coach> coaches = coachDataAccess.getAll();

            System.out.println("Coachs récupérés : " + coaches.size());

            for (Coach coach : coaches) {
                System.out.println(
                        coach.getId()
                                + " - "
                                + coach.getFirstName()
                                + " "
                                + coach.getLastName()
                                + " - diplômé : "
                                + coach.getHasDegree()
                );
            }

            if (!coaches.isEmpty()) {
                Coach coach = coachDataAccess.getById(coaches.get(0).getId());

                System.out.println(
                        "Coach récupéré : "
                                + coach.getFirstName()
                                + " "
                                + coach.getLastName()
                );
            }

            List<Coach> coachesBySpeciality = coachDataAccess.getBySpecialityName("Musculation");

            System.out.println("Coachs spécialisés en Musculation : " + coachesBySpeciality.size());

            for (Coach coach : coachesBySpeciality) {
                System.out.println(
                        "- "
                                + coach.getFirstName()
                                + " "
                                + coach.getLastName()
                );
            }

        } catch (ReadCoachException exception) {
            System.out.println("Erreur pendant le test CoachDA : " + exception.getMessage());
        }
    }
}