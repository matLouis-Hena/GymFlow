package testPackage;

import businessPackage.AppointmentManager;
import dataAccessPackage.coachAvailabilityDataAccess.*;
import dataAccessPackage.gymMemberDataAccess.*;
import dataAccessPackage.roomDataAccess.*;
import modelPackage.*;

import java.time.LocalDate;
import java.util.List;

public class TestAppointmentManager {

    public static void main(String[] args) {
        AppointmentManager appointmentManager = new AppointmentManager();

        IGymMemberDA gymMemberDataAccess = new GymMemberDBAccess();
        ICoachAvailabilityDA coachAvailabilityDataAccess = new CoachAvailabilityDBAccess();
        IRoomDA roomDataAccess = new RoomDBAccess();

        int insertedAppointmentId = -1;

        try {
            String specialityName = "Musculation";

            List<GymMember> members = gymMemberDataAccess.getAll();

            List<CoachAvailability> availabilities =
                    coachAvailabilityDataAccess.getAvailableBySpecialityAndDateRange(
                            specialityName,
                            LocalDate.now().minusDays(1),
                            LocalDate.now().plusMonths(3)
                    );

            List<Room> rooms = roomDataAccess.getAll();

            if (members.isEmpty()) {
                System.out.println("Aucun membre disponible pour tester AppointmentManager.");
                return;
            }

            if (availabilities.isEmpty()) {
                System.out.println("Aucune disponibilité disponible pour la spécialité " + specialityName + ".");
                return;
            }

            if (rooms.isEmpty()) {
                System.out.println("Aucune salle disponible pour tester AppointmentManager.");
                return;
            }

            GymMember member = findActiveMember(members);
            CoachAvailability availability = findAvailableAvailability(availabilities);
            Room room = rooms.get(0);

            insertedAppointmentId = appointmentManager.bookAppointment(
                    member.getId(),
                    availability.getId(),
                    specialityName,
                    room.getId(),
                    "Objectif manager : prise de masse"
            );

            System.out.println("Rendez-vous créé via manager : " + insertedAppointmentId);

            Appointment appointment = appointmentManager.getAppointmentById(insertedAppointmentId);

            System.out.println(
                    "Rendez-vous récupéré : "
                            + appointment.getMember().getFirstName()
                            + " "
                            + appointment.getMember().getLastName()
                            + " avec "
                            + appointment.getAvailability().getCoach().getFirstName()
                            + " "
                            + appointment.getAvailability().getCoach().getLastName()
            );

            appointmentManager.confirmAppointment(insertedAppointmentId);

            Appointment confirmedAppointment = appointmentManager.getAppointmentById(insertedAppointmentId);

            System.out.println(
                    "Statut après confirmation : "
                            + confirmedAppointment.getStatus()
            );

            appointmentManager.cancelAppointmentByMember(
                    insertedAppointmentId,
                    "Annulation test côté membre"
            );

            Appointment cancelledAppointment = appointmentManager.getAppointmentById(insertedAppointmentId);

            System.out.println(
                    "Statut après annulation : "
                            + cancelledAppointment.getStatus()
                            + " - raison : "
                            + cancelledAppointment.getCancellationReason()
            );

            appointmentManager.deleteAppointment(insertedAppointmentId);
            insertedAppointmentId = -1;

            System.out.println("Rendez-vous supprimé via manager.");

        } catch (Exception exception) {
            System.out.println("Erreur pendant le test AppointmentManager : " + exception.getMessage());
            cleanInsertedAppointment(appointmentManager, insertedAppointmentId);
        }
    }

    private static GymMember findActiveMember(List<GymMember> members) {
        for (GymMember member : members) {
            if (member.getIsActive()) {
                return member;
            }
        }

        throw new IllegalStateException("Aucun membre actif trouvé.");
    }

    private static CoachAvailability findAvailableAvailability(List<CoachAvailability> availabilities) {
        for (CoachAvailability availability : availabilities) {
            if (!availability.isBooked()) {
                return availability;
            }
        }

        throw new IllegalStateException("Aucune disponibilité libre trouvée.");
    }

    private static void cleanInsertedAppointment(AppointmentManager appointmentManager, int appointmentId) {
        if (appointmentId <= 0) {
            return;
        }

        try {
            appointmentManager.deleteAppointment(appointmentId);
            System.out.println("Nettoyage : rendez-vous supprimé après erreur.");
        } catch (Exception exception) {
            System.out.println("Nettoyage impossible : " + exception.getMessage());
        }
    }
}