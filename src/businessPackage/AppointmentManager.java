package businessPackage;

import dataAccessPackage.appointmentDataAccess.*;
import dataAccessPackage.coachAvailabilityDataAccess.*;
import dataAccessPackage.gymMemberDataAccess.*;
import dataAccessPackage.roomDataAccess.*;
import exceptionPackage.appointment.*;
import exceptionPackage.coachAvailability.*;
import exceptionPackage.gymMember.*;
import exceptionPackage.room.*;
import modelPackage.*;

import java.util.List;

public class AppointmentManager {

    private final IAppointmentDA appointmentDataAccess;
    private final IGymMemberDA gymMemberDataAccess;
    private final ICoachAvailabilityDA coachAvailabilityDataAccess;
    private final IRoomDA roomDataAccess;

    public AppointmentManager() {
        this.appointmentDataAccess = new AppointmentDBAccess();
        this.gymMemberDataAccess = new GymMemberDBAccess();
        this.coachAvailabilityDataAccess = new CoachAvailabilityDBAccess();
        this.roomDataAccess = new RoomDBAccess();
    }

    public AppointmentManager(
            IAppointmentDA appointmentDataAccess,
            IGymMemberDA gymMemberDataAccess,
            ICoachAvailabilityDA coachAvailabilityDataAccess,
            IRoomDA roomDataAccess
    ) {
        this.appointmentDataAccess = appointmentDataAccess;
        this.gymMemberDataAccess = gymMemberDataAccess;
        this.coachAvailabilityDataAccess = coachAvailabilityDataAccess;
        this.roomDataAccess = roomDataAccess;
    }

    public int bookAppointment(
            int memberId,
            int availabilityId,
            String specialityName,
            Integer roomId,
            String objective
    ) throws ReadGymMemberException,
            ReadCoachAvailabilityException,
            ReadRoomException,
            ReadAppointmentException,
            AddAppointmentException,
            UpdateCoachAvailabilityException,
            AppointmentBusinessException {
        validateId(memberId, "memberId", "L'identifiant du membre doit être supérieur à 0.");
        validateId(availabilityId, "availabilityId", "L'identifiant de la disponibilité doit être supérieur à 0.");
        validateSpecialityName(specialityName);

        GymMember member = gymMemberDataAccess.getById(memberId);

        if (member == null) {
            throw new AppointmentBusinessException(
                    String.valueOf(memberId),
                    "Le membre sélectionné est introuvable."
            );
        }

        if (!member.getIsActive()) {
            throw new AppointmentBusinessException(
                    String.valueOf(memberId),
                    "Le membre sélectionné est inactif."
            );
        }

        CoachAvailability availability = coachAvailabilityDataAccess.getById(availabilityId);

        if (availability == null) {
            throw new AppointmentBusinessException(
                    String.valueOf(availabilityId),
                    "La disponibilité sélectionnée est introuvable."
            );
        }

        if (availability.isBooked()) {
            throw new AppointmentBusinessException(
                    String.valueOf(availabilityId),
                    "Cette disponibilité est déjà réservée."
            );
        }

        boolean coachQualified = coachAvailabilityDataAccess.isCoachQualifiedForSpeciality(
                availability.getId(),
                specialityName
        );

        if (!coachQualified) {
            throw new AppointmentBusinessException(
                    specialityName,
                    "Le coach du créneau sélectionné n'est pas qualifié pour cette spécialité."
            );
        }

        boolean alreadyHasAppointment = appointmentDataAccess.existsForMemberOnDate(
                member.getId(),
                availability.getAvailableDate()
        );

        if (alreadyHasAppointment) {
            throw new AppointmentBusinessException(
                    String.valueOf(member.getId()),
                    "Ce membre a déjà un rendez-vous ce jour-là."
            );
        }

        Room room = getRoomIfNeeded(roomId);

        Appointment appointment = new Appointment(
                0,
                member,
                availability,
                objective,
                room,
                AppointmentStatus.PENDING,
                null
        );

        int appointmentId = appointmentDataAccess.insert(appointment);

        try {
            coachAvailabilityDataAccess.markAsBooked(availability.getId());
        } catch (UpdateCoachAvailabilityException exception) {
            cleanAppointmentAfterBookingFailure(appointmentId);
            throw exception;
        }

        return appointmentId;
    }

    public Appointment getAppointmentById(int id) throws ReadAppointmentException, AppointmentBusinessException {
        validateId(id, "appointmentId", "L'identifiant du rendez-vous doit être supérieur à 0.");

        Appointment appointment = appointmentDataAccess.getById(id);

        if (appointment == null) {
            throw new AppointmentBusinessException(
                    String.valueOf(id),
                    "Aucun rendez-vous trouvé avec cet identifiant."
            );
        }

        return appointment;
    }

    public List<Appointment> getAllAppointments() throws ReadAppointmentException {
        return appointmentDataAccess.getAll();
    }

    public List<Appointment> getAppointmentsByMemberId(int memberId)
            throws ReadAppointmentException, AppointmentBusinessException {
        validateId(memberId, "memberId", "L'identifiant du membre doit être supérieur à 0.");

        return appointmentDataAccess.getByMemberId(memberId);
    }

    public void confirmAppointment(int appointmentId)
            throws ReadAppointmentException,
            UpdateAppointmentException,
            AppointmentBusinessException {
        Appointment appointment = getAppointmentById(appointmentId);

        if (isCancelled(appointment)) {
            throw new AppointmentBusinessException(
                    String.valueOf(appointmentId),
                    "Un rendez-vous annulé ne peut pas être confirmé."
            );
        }

        appointmentDataAccess.updateStatus(
                appointmentId,
                AppointmentStatus.CONFIRMED.ordinal(),
                null
        );
    }

    public void cancelAppointmentByMember(int appointmentId, String cancellationReason)
            throws ReadAppointmentException,
            UpdateAppointmentException,
            UpdateCoachAvailabilityException,
            AppointmentBusinessException {
        cancelAppointment(
                appointmentId,
                AppointmentStatus.CANCELLED_BY_MEMBER,
                cancellationReason
        );
    }

    public void cancelAppointmentByCoach(int appointmentId, String cancellationReason)
            throws ReadAppointmentException,
            UpdateAppointmentException,
            UpdateCoachAvailabilityException,
            AppointmentBusinessException {
        cancelAppointment(
                appointmentId,
                AppointmentStatus.CANCELLED_BY_COACH,
                cancellationReason
        );
    }

    public void deleteAppointment(int appointmentId)
            throws ReadAppointmentException,
            DeleteAppointmentException,
            UpdateCoachAvailabilityException,
            AppointmentBusinessException {
        Appointment appointment = getAppointmentById(appointmentId);

        appointmentDataAccess.delete(appointmentId);

        if (appointment.getAvailability() != null) {
            coachAvailabilityDataAccess.markAsAvailable(appointment.getAvailability().getId());
        }
    }

    public void deleteAppointmentsByMemberId(int memberId)
            throws ReadAppointmentException,
            DeleteAppointmentException,
            UpdateCoachAvailabilityException,
            AppointmentBusinessException {
        validateId(memberId, "memberId", "L'identifiant du membre doit être supérieur à 0.");

        List<Appointment> appointments = appointmentDataAccess.getByMemberId(memberId);

        for (Appointment appointment : appointments) {
            if (appointment.getAvailability() != null) {
                coachAvailabilityDataAccess.markAsAvailable(
                        appointment.getAvailability().getId()
                );
            }
        }

        appointmentDataAccess.deleteByMemberId(memberId);
    }

    private void cancelAppointment(
            int appointmentId,
            AppointmentStatus cancelledStatus,
            String cancellationReason
    ) throws ReadAppointmentException,
            UpdateAppointmentException,
            UpdateCoachAvailabilityException,
            AppointmentBusinessException {
        Appointment appointment = getAppointmentById(appointmentId);

        if (isCancelled(appointment)) {
            throw new AppointmentBusinessException(
                    String.valueOf(appointmentId),
                    "Ce rendez-vous est déjà annulé."
            );
        }

        if (cancellationReason == null || cancellationReason.isBlank()) {
            throw new AppointmentBusinessException(
                    "cancellationReason",
                    "La raison d'annulation est obligatoire."
            );
        }

        appointmentDataAccess.updateStatus(
                appointmentId,
                cancelledStatus.ordinal(),
                cancellationReason
        );

        coachAvailabilityDataAccess.markAsAvailable(appointment.getAvailability().getId());
    }

    private Room getRoomIfNeeded(Integer roomId)
            throws ReadRoomException, AppointmentBusinessException {
        if (roomId == null || roomId <= 0) {
            return null;
        }

        Room room = roomDataAccess.getById(roomId);

        if (room == null) {
            throw new AppointmentBusinessException(
                    String.valueOf(roomId),
                    "La salle sélectionnée est introuvable."
            );
        }

        return room;
    }

    private boolean isCancelled(Appointment appointment) {
        return appointment.getStatus() == AppointmentStatus.CANCELLED_BY_MEMBER
                || appointment.getStatus() == AppointmentStatus.CANCELLED_BY_COACH;
    }

    private void validateId(int id, String wrongValue, String message) throws AppointmentBusinessException {
        if (id <= 0) {
            throw new AppointmentBusinessException(wrongValue, message);
        }
    }

    private void validateSpecialityName(String specialityName) throws AppointmentBusinessException {
        if (specialityName == null || specialityName.isBlank()) {
            throw new AppointmentBusinessException(
                    "specialityName",
                    "La spécialité choisie est obligatoire."
            );
        }
    }

    private void cleanAppointmentAfterBookingFailure(int appointmentId) {
        try {
            appointmentDataAccess.delete(appointmentId);
        } catch (DeleteAppointmentException ignored) {
            // Nettoyage silencieux côté business.
        }
    }
}
