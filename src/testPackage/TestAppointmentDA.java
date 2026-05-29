package testPackage;

import dataAccessPackage.appointmentDataAccess.AppointmentDBAccess;
import dataAccessPackage.appointmentDataAccess.IAppointmentDA;
import dataAccessPackage.coachAvailabilityDataAccess.CoachAvailabilityDBAccess;
import dataAccessPackage.coachAvailabilityDataAccess.ICoachAvailabilityDA;
import dataAccessPackage.gymMemberDataAccess.GymMemberDBAccess;
import dataAccessPackage.gymMemberDataAccess.IGymMemberDA;
import dataAccessPackage.roomDataAccess.IRoomDA;
import dataAccessPackage.roomDataAccess.RoomDBAccess;
import modelPackage.*;

import java.util.List;

public class TestAppointmentDA {

    public static void main(String[] args) {
        IAppointmentDA appointmentDataAccess = new AppointmentDBAccess();
        IGymMemberDA gymMemberDataAccess = new GymMemberDBAccess();
        ICoachAvailabilityDA coachAvailabilityDataAccess = new CoachAvailabilityDBAccess();
        IRoomDA roomDataAccess = new RoomDBAccess();

        int insertedAppointmentId = -1;

        try {
            List<GymMember> members = gymMemberDataAccess.getAll();
            List<CoachAvailability> availabilities = coachAvailabilityDataAccess.getAll();
            List<Room> rooms = roomDataAccess.getAll();

            if (members.isEmpty()) {
                System.out.println("Aucun membre disponible pour tester les rendez-vous.");
                return;
            }

            if (availabilities.isEmpty()) {
                System.out.println("Aucune disponibilité coach disponible pour tester les rendez-vous.");
                return;
            }

            if (rooms.isEmpty()) {
                System.out.println("Aucune salle disponible pour tester les rendez-vous.");
                return;
            }

            GymMember member = members.get(0);
            CoachAvailability availability = findAvailableAvailability(availabilities);
            Room room = rooms.get(0);

            Appointment appointment = new Appointment(
                    0,
                    member,
                    availability,
                    "Objectif test : prise de masse",
                    room,
                    AppointmentStatus.PENDING,
                    null
            );

            insertedAppointmentId = appointmentDataAccess.insert(appointment);
            System.out.println("Rendez-vous inséré : " + insertedAppointmentId);

            Appointment retrievedAppointment = appointmentDataAccess.getById(insertedAppointmentId);

            System.out.println(
                    "Rendez-vous récupéré : "
                            + retrievedAppointment.getMember().getFirstName()
                            + " "
                            + retrievedAppointment.getMember().getLastName()
                            + " avec "
                            + retrievedAppointment.getAvailability().getCoach().getFirstName()
                            + " "
                            + retrievedAppointment.getAvailability().getCoach().getLastName()
            );

            boolean exists = appointmentDataAccess.existsForMemberOnDate(
                    member.getId(),
                    availability.getAvailableDate()
            );

            System.out.println("Le membre a déjà un rendez-vous ce jour-là : " + exists);

            List<Appointment> memberAppointments = appointmentDataAccess.getByMemberId(member.getId());

            System.out.println("Rendez-vous du membre : " + memberAppointments.size());

            appointmentDataAccess.updateStatus(
                    insertedAppointmentId,
                    AppointmentStatus.CONFIRMED.ordinal(),
                    null
            );

            Appointment updatedAppointment = appointmentDataAccess.getById(insertedAppointmentId);

            System.out.println(
                    "Statut avant update : "
                            + appointment.getStatus()
            );

            System.out.println(
                    "Statut après update : "
                            + updatedAppointment.getStatus()
            );

        } catch (Exception exception) {
            System.out.println("Erreur pendant le test AppointmentDA : " + exception.getMessage());
            cleanInsertedAppointment(appointmentDataAccess, insertedAppointmentId);
        }
    }

    private static CoachAvailability findAvailableAvailability(List<CoachAvailability> availabilities) {
        for (CoachAvailability availability : availabilities) {
            if (!availability.isBooked()) {
                return availability;
            }
        }

        return availabilities.get(0);
    }

    private static void cleanInsertedAppointment(IAppointmentDA appointmentDataAccess, int appointmentId) {
        if (appointmentId <= 0) {
            return;
        }

        try {
            appointmentDataAccess.delete(appointmentId);
            System.out.println("Nettoyage : rendez-vous supprimé après erreur.");
        } catch (Exception exception) {
            System.out.println("Nettoyage impossible : " + exception.getMessage());
        }
    }
}