package testPackage;

import businessPackage.QualificationManager;
import dataAccessPackage.coachDataAccess.*;
import modelPackage.Coach;
import modelPackage.Speciality;

import java.util.List;

public class TestQualificationDA {

    public static void main(String[] args) {
        QualificationManager qualificationManager = new QualificationManager();
        ICoachDA coachDataAccess = new CoachDBAccess();

        try {
            List<Coach> coaches = coachDataAccess.getAll();

            if (coaches.isEmpty()) {
                System.out.println("Aucun coach disponible pour tester QualificationDA.");
                return;
            }

            Coach coach = coaches.get(0);

            System.out.println(
                    "Coach testé : "
                            + coach.getId()
                            + " - "
                            + coach.getFirstName()
                            + " "
                            + coach.getLastName()
            );

            List<Speciality> specialities =
                    qualificationManager.getSpecialitiesByCoachId(coach.getId());

            System.out.println("Spécialités du coach : " + specialities.size());

            for (Speciality speciality : specialities) {
                System.out.println(
                        "- "
                                + speciality.getName()
                                + " : "
                                + speciality.getDescription()
                );
            }

            String specialityNames =
                    qualificationManager.getSpecialityNamesByCoachId(coach.getId());

            System.out.println("Affichage view : " + specialityNames);

            boolean qualifiedForMusculation =
                    qualificationManager.isCoachQualifiedForSpeciality(
                            coach.getId(),
                            "Musculation"
                    );

            System.out.println("Qualifié en Musculation : " + qualifiedForMusculation);

        } catch (Exception exception) {
            System.out.println("Erreur pendant le test QualificationDA : " + exception.getMessage());
        }
    }
}