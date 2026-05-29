package dataAccessPackage.coachAvailabilityDataAccess;

import exceptionPackage.coachAvailability.ReadCoachAvailabilityException;
import exceptionPackage.coachAvailability.UpdateCoachAvailabilityException;
import modelPackage.CoachAvailability;

import java.time.LocalDate;
import java.util.List;

public interface ICoachAvailabilityDA {

    List<CoachAvailability> getAll() throws ReadCoachAvailabilityException;

    CoachAvailability getById(int id) throws ReadCoachAvailabilityException;

    List<CoachAvailability> getByCoachId(int coachId) throws ReadCoachAvailabilityException;

    void insert(int coachId, LocalDate date, java.sql.Time startTime, java.sql.Time endTime)
            throws UpdateCoachAvailabilityException;

    void delete(int id) throws UpdateCoachAvailabilityException;

    List<CoachAvailability> getAvailableBySpecialityAndDateRange(
            String specialityName,
            LocalDate startDate,
            LocalDate endDate
    ) throws ReadCoachAvailabilityException;

    boolean isCoachQualifiedForSpeciality(
            int availabilityId,
            String specialityName
    ) throws ReadCoachAvailabilityException;

    void markAsBooked(int id) throws UpdateCoachAvailabilityException;

    void markAsAvailable(int id) throws UpdateCoachAvailabilityException;
}
