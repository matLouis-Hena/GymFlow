package businessPackage;

import dataAccessPackage.qualificationDataAccess.*;
import exceptionPackage.qualification.*;
import modelPackage.Speciality;

import java.util.List;

public class QualificationManager {

    private final IQualificationDA qualificationDataAccess;

    public QualificationManager() {
        this.qualificationDataAccess = new QualificationDBAccess();
    }

    public QualificationManager(IQualificationDA qualificationDataAccess) {
        this.qualificationDataAccess = qualificationDataAccess;
    }

    public List<Speciality> getSpecialitiesByCoachId(int coachId)
            throws ReadQualificationException {
        validateCoachId(coachId);
        return qualificationDataAccess.getSpecialitiesByCoachId(coachId);
    }

    public String getSpecialityNamesByCoachId(int coachId)
            throws ReadQualificationException {
        List<Speciality> specialities = getSpecialitiesByCoachId(coachId);

        if (specialities.isEmpty()) {
            return "Aucune spécialité";
        }

        StringBuilder specialityNames = new StringBuilder();

        for (int index = 0; index < specialities.size(); index++) {
            specialityNames.append(specialities.get(index).getName());

            if (index < specialities.size() - 1) {
                specialityNames.append(", ");
            }
        }

        return specialityNames.toString();
    }

    public boolean isCoachQualifiedForSpeciality(int coachId, String specialityName)
            throws ReadQualificationException {
        validateCoachId(coachId);
        validateSpecialityName(specialityName);

        return qualificationDataAccess.isCoachQualifiedForSpeciality(coachId, specialityName);
    }

    private void validateCoachId(int coachId) throws ReadQualificationException {
        if (coachId <= 0) {
            throw new ReadQualificationException(
                    String.valueOf(coachId),
                    "L'identifiant du coach doit être supérieur à 0."
            );
        }
    }

    private void validateSpecialityName(String specialityName)
            throws ReadQualificationException {
        if (specialityName == null || specialityName.isBlank()) {
            throw new ReadQualificationException(
                    "specialityName",
                    "Le nom de la spécialité est obligatoire."
            );
        }
    }
}