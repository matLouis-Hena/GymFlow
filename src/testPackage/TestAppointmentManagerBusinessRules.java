package testPackage;

import businessPackage.AppointmentManager;
import dataAccessPackage.appointmentDataAccess.AppointmentDBAccess;
import dataAccessPackage.appointmentDataAccess.IAppointmentDA;
import dataAccessPackage.coachAvailabilityDataAccess.CoachAvailabilityDBAccess;
import dataAccessPackage.coachAvailabilityDataAccess.ICoachAvailabilityDA;
import dataAccessPackage.gymMemberDataAccess.GymMemberDBAccess;
import dataAccessPackage.gymMemberDataAccess.IGymMemberDA;
import dataAccessPackage.roomDataAccess.IRoomDA;
import dataAccessPackage.roomDataAccess.RoomDBAccess;
import exceptionPackage.appointment.AppointmentBusinessException;
import modelPackage.CoachAvailability;
import modelPackage.GymMember;
import modelPackage.Room;

import java.time.LocalDate;
import java.util.List;

public class TestAppointmentManagerBusinessRules {

    public static void main(String[] args) {
        AppointmentManager appointmentManager = new AppointmentManager();

        IGymMemberDA gymMemberDataAccess = new GymMemberDBAccess();
        ICoachAvailabilityDA coachAvailabilityDataAccess = new CoachAvailabilityDBAccess();
        IRoomDA roomDataAccess = new RoomDBAccess();
        IAppointmentDA appointmentDataAccess = new AppointmentDBAccess();

        try {
            String validSpecialityName = "Musculation";
            String invalidSpecialityName = "SpecialiteImpossibleTest";

            List<CoachAvailability> availabilities =
                    coachAvailabilityDataAccess.getAvailableBySpecialityAndDateRange(
                            validSpecialityName,
                            LocalDate.now().minusDays(1),
                            LocalDate.now().plusMonths(3)
                    );

            if (availabilities.isEmpty()) {
                System.out.println("Aucune disponibilité libre pour tester les règles métier.");
                return;
            }

            CoachAvailability availability = availabilities.get(0);

            List<GymMember> members = gymMemberDataAccess.getAll();

            GymMember member = findActiveMemberWithoutAppointmentOnDate(
                    members,
                    appointmentDataAccess,
                    availability.getAvailableDate()
            );

            if (member == null) {
                System.out.println("Aucun membre actif sans rendez-vous ce jour-là pour tester.");
                return;
            }

            Integer roomId = getFirstRoomIdOrNull(roomDataAccess);

            testWrongSpeciality(
                    appointmentManager,
                    member,
                    availability,
                    invalidSpecialityName,
                    roomId
            );

            testAlreadyBookedAvailability(
                    appointmentManager,
                    member,
                    availability,
                    validSpecialityName,
                    roomId
            );

        } catch (Exception exception) {
            System.out.println("Erreur pendant le test métier AppointmentManager : " + exception.getMessage());
        }
    }

    private static void testWrongSpeciality(
            AppointmentManager appointmentManager,
            GymMember member,
            CoachAvailability availability,
            String invalidSpecialityName,
            Integer roomId
    ) {
        int unexpectedAppointmentId = -1;

        try {
            unexpectedAppointmentId = appointmentManager.bookAppointment(
                    member.getId(),
                    availability.getId(),
                    invalidSpecialityName,
                    roomId,
                    "Test spécialité invalide"
            );

            System.out.println("ERREUR : le rendez-vous a été créé alors que la spécialité est invalide.");

        } catch (AppointmentBusinessException exception) {
            System.out.println("Erreur attendue spécialité : " + exception.getMessage());

        } catch (Exception exception) {
            System.out.println("Erreur inattendue spécialité : " + exception.getMessage());

        } finally {
            cleanAppointment(appointmentManager, unexpectedAppointmentId);
        }
    }

    private static void testAlreadyBookedAvailability(
            AppointmentManager appointmentManager,
            GymMember member,
            CoachAvailability availability,
            String specialityName,
            Integer roomId
    ) {
        int firstAppointmentId = -1;
        int secondAppointmentId = -1;

        try {
            firstAppointmentId = appointmentManager.bookAppointment(
                    member.getId(),
                    availability.getId(),
                    specialityName,
                    roomId,
                    "Premier rendez-vous test"
            );

            System.out.println("Premier rendez-vous créé : " + firstAppointmentId);

            secondAppointmentId = appointmentManager.bookAppointment(
                    member.getId(),
                    availability.getId(),
                    specialityName,
                    roomId,
                    "Deuxième rendez-vous test"
            );

            System.out.println("ERREUR : le deuxième rendez-vous a été créé sur un créneau déjà réservé.");

        } catch (AppointmentBusinessException exception) {
            System.out.println("Erreur attendue créneau réservé : " + exception.getMessage());

        } catch (Exception exception) {
            System.out.println("Erreur inattendue créneau réservé : " + exception.getMessage());

        } finally {
            cleanAppointment(appointmentManager, secondAppointmentId);
            cleanAppointment(appointmentManager, firstAppointmentId);
        }
    }

    private static GymMember findActiveMemberWithoutAppointmentOnDate(
            List<GymMember> members,
            IAppointmentDA appointmentDataAccess,
            LocalDate date
    ) throws Exception {
        for (GymMember member : members) {
            if (!appointmentDataAccess.existsForMemberOnDate(member.getId(), date)) {
                return member;
            }
        }

        return null;
    }

    private static Integer getFirstRoomIdOrNull(IRoomDA roomDataAccess) throws Exception {
        List<Room> rooms = roomDataAccess.getAll();

        if (rooms.isEmpty()) {
            return null;
        }

        return rooms.get(0).getId();
    }

    private static void cleanAppointment(AppointmentManager appointmentManager, int appointmentId) {
        if (appointmentId <= 0) {
            return;
        }

        try {
            appointmentManager.deleteAppointment(appointmentId);
            System.out.println("Nettoyage : rendez-vous supprimé.");
        } catch (Exception exception) {
            System.out.println("Nettoyage impossible : " + exception.getMessage());
        }
    }
}
