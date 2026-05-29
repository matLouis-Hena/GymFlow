package businessPackage;

import dataAccessPackage.coachAvailabilityDataAccess.CoachAvailabilityDBAccess;
import dataAccessPackage.coachAvailabilityDataAccess.ICoachAvailabilityDA;
import exceptionPackage.coachAvailability.ReadCoachAvailabilityException;
import exceptionPackage.coachAvailability.UpdateCoachAvailabilityException;
import modelPackage.CoachAvailability;

import java.time.LocalDate;
import java.util.List;

public class CoachAvailabilityManager {

    private final ICoachAvailabilityDA coachAvailabilityDataAccess;

    public CoachAvailabilityManager() {
        this.coachAvailabilityDataAccess = new CoachAvailabilityDBAccess();
    }

    public CoachAvailabilityManager(ICoachAvailabilityDA coachAvailabilityDataAccess) {
        this.coachAvailabilityDataAccess = coachAvailabilityDataAccess;
    }

    public List<CoachAvailability> getAllAvailabilities() throws ReadCoachAvailabilityException {
        return coachAvailabilityDataAccess.getAll();
    }

    public CoachAvailability getAvailabilityById(int id) throws ReadCoachAvailabilityException {
        validateIdForRead(id);

        CoachAvailability availability = coachAvailabilityDataAccess.getById(id);

        if (availability == null) {
            throw new ReadCoachAvailabilityException(
                    String.valueOf(id),
                    "Aucune disponibilité trouvée avec cet identifiant."
            );
        }

        return availability;
    }

    public List<CoachAvailability> getAvailableBySpecialityAndDateRange(
            String specialityName,
            LocalDate startDate,
            LocalDate endDate
    ) throws ReadCoachAvailabilityException {
        validateSearchCriteria(specialityName, startDate, endDate);

        return coachAvailabilityDataAccess.getAvailableBySpecialityAndDateRange(
                specialityName,
                startDate,
                endDate
        );
    }

    public void markAsBooked(int id) throws UpdateCoachAvailabilityException {
        validateIdForUpdate(id);
        coachAvailabilityDataAccess.markAsBooked(id);
    }

    public void markAsAvailable(int id) throws UpdateCoachAvailabilityException {
        validateIdForUpdate(id);
        coachAvailabilityDataAccess.markAsAvailable(id);
    }

    private void validateSearchCriteria(String specialityName, LocalDate startDate, LocalDate endDate)
            throws ReadCoachAvailabilityException {
        if (specialityName == null || specialityName.isBlank()) {
            throw new ReadCoachAvailabilityException(
                    "specialityName",
                    "Le nom de la spécialité est obligatoire."
            );
        }

        if (startDate == null) {
            throw new ReadCoachAvailabilityException(
                    "startDate",
                    "La date de début est obligatoire."
            );
        }

        if (endDate == null) {
            throw new ReadCoachAvailabilityException(
                    "endDate",
                    "La date de fin est obligatoire."
            );
        }

        if (endDate.isBefore(startDate)) {
            throw new ReadCoachAvailabilityException(
                    "endDate",
                    "La date de fin ne peut pas être avant la date de début."
            );
        }
    }

    private void validateIdForRead(int id) throws ReadCoachAvailabilityException {
        if (id <= 0) {
            throw new ReadCoachAvailabilityException(
                    String.valueOf(id),
                    "L'identifiant de la disponibilité doit être supérieur à 0."
            );
        }
    }

    private void validateIdForUpdate(int id) throws UpdateCoachAvailabilityException {
        if (id <= 0) {
            throw new UpdateCoachAvailabilityException(
                    String.valueOf(id),
                    "L'identifiant de la disponibilité doit être supérieur à 0."
            );
        }
    }
}