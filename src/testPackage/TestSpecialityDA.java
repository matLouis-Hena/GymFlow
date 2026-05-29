package testPackage;

import dataAccessPackage.specialityDataAccess.ISpecialityDA;
import dataAccessPackage.specialityDataAccess.SpecialityDBAccess;
import exceptionPackage.speciality.ReadSpecialityException;
import modelPackage.Speciality;

import java.util.List;

public class TestSpecialityDA {

    public static void main(String[] args) {
        ISpecialityDA specialityDataAccess = new SpecialityDBAccess();

        try {
            List<Speciality> specialities = specialityDataAccess.getAll();

            System.out.println("Spécialités récupérées : " + specialities.size());

            for (Speciality speciality : specialities) {
                System.out.println(
                        speciality.getName()
                                + " - "
                                + speciality.getDescription()
                );
            }

            if (!specialities.isEmpty()) {
                Speciality speciality = specialityDataAccess.getByName(specialities.getFirst().getName());

                System.out.println(
                        "Spécialité récupérée : "
                                + speciality.getName()
                );
            }

        } catch (ReadSpecialityException exception) {
            System.out.println("Erreur pendant le test SpecialityDA : " + exception.getMessage());
        }
    }
}