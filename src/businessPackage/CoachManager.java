package businessPackage;

import dataAccessPackage.coachDataAccess.CoachDBAccess;
import dataAccessPackage.coachDataAccess.ICoachDA;
import exceptionPackage.coach.ReadCoachException;
import modelPackage.Coach;

import java.util.List;

public class CoachManager {

    private final ICoachDA coachDataAccess;

    public CoachManager() {
        this.coachDataAccess = new CoachDBAccess();
    }

    public CoachManager(ICoachDA coachDataAccess) {
        this.coachDataAccess = coachDataAccess;
    }

    public List<Coach> getAllCoaches() throws ReadCoachException {
        return coachDataAccess.getAll();
    }

    public Coach getCoachById(int id) throws ReadCoachException {
        validateId(id);

        Coach coach = coachDataAccess.getById(id);

        if (coach == null) {
            throw new ReadCoachException(
                    String.valueOf(id),
                    "Aucun coach trouvé avec cet identifiant."
            );
        }

        return coach;
    }

    public List<Coach> getCoachesBySpecialityName(String specialityName) throws ReadCoachException {
        if (specialityName == null || specialityName.isBlank()) {
            throw new ReadCoachException(
                    "specialityName",
                    "Le nom de la spécialité est obligatoire."
            );
        }

        return coachDataAccess.getBySpecialityName(specialityName);
    }

    private void validateId(int id) throws ReadCoachException {
        if (id <= 0) {
            throw new ReadCoachException(
                    String.valueOf(id),
                    "L'identifiant du coach doit être supérieur à 0."
            );
        }
    }
}