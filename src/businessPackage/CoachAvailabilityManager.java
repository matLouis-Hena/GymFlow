package businessPackage;

import dataAccessPackage.coachAvailabilityDataAccess.CoachAvailabilityDBAccess;
import dataAccessPackage.coachAvailabilityDataAccess.ICoachAvailabilityDA;
import exceptionPackage.coachAvailability.ReadCoachAvailabilityException;
import exceptionPackage.coachAvailability.UpdateCoachAvailabilityException;
import modelPackage.CoachAvailability;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
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

    public List<CoachAvailability> getAvailabilitiesByCoachId(int coachId)
            throws ReadCoachAvailabilityException {
        validateIdForRead(coachId);
        return coachAvailabilityDataAccess.getByCoachId(coachId);
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

    public void addAvailability(int coachId, LocalDate date, LocalTime startTime, LocalTime endTime)
            throws UpdateCoachAvailabilityException {
        validateIdForUpdate(coachId);

        if (date == null) {
            throw new UpdateCoachAvailabilityException(
                    "date",
                    "La date est obligatoire."
            );
        }

        if (date.isBefore(LocalDate.now())) {
            throw new UpdateCoachAvailabilityException(
                    String.valueOf(date),
                    "La date ne peut pas etre dans le passe."
            );
        }

        if (startTime == null || endTime == null) {
            throw new UpdateCoachAvailabilityException(
                    "time",
                    "Les heures sont obligatoires."
            );
        }

        if (!endTime.isAfter(startTime)) {
            throw new UpdateCoachAvailabilityException(
                    String.valueOf(endTime),
                    "L'heure de fin doit etre apres l'heure de debut."
            );
        }

        coachAvailabilityDataAccess.insert(
                coachId,
                date,
                Time.valueOf(startTime),
                Time.valueOf(endTime)
        );
    }

    public void deleteAvailability(int id) throws UpdateCoachAvailabilityException {
        validateIdForUpdate(id);
        coachAvailabilityDataAccess.delete(id);
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
