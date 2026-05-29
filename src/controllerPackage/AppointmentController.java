package controllerPackage;

import businessPackage.AppointmentManager;
import businessPackage.RoomManager;
import businessPackage.SearchManager;
import businessPackage.SpecialityManager;
import exceptionPackage.appointment.*;
import exceptionPackage.coachAvailability.*;
import exceptionPackage.gymMember.ReadGymMemberException;
import exceptionPackage.room.ReadRoomException;
import exceptionPackage.search.SearchException;
import exceptionPackage.speciality.ReadSpecialityException;
import modelPackage.Appointment;
import modelPackage.Room;
import modelPackage.Speciality;
import modelPackage.searchResult.AvailableCoachSearchResult;

import viewPackage.MainView;

import java.time.LocalDate;
import java.util.List;

public class AppointmentController {

    private final AppointmentManager appointmentManager;
    private final SearchManager searchManager;
    private final SpecialityManager specialityManager;
    private final RoomManager roomManager;
    private final MainView mainView;

    public AppointmentController(
            AppointmentManager appointmentManager,
            SearchManager searchManager,
            SpecialityManager specialityManager,
            RoomManager roomManager,
            MainView mainView
    ) {
        this.appointmentManager = appointmentManager;
        this.searchManager = searchManager;
        this.specialityManager = specialityManager;
        this.roomManager = roomManager;
        this.mainView = mainView;
    }

    public void showBookingForm() {
        try {
            List<Speciality> specialities = specialityManager.getAllSpecialities();
            List<Room> rooms = roomManager.getAllRooms();
            mainView.showAppointmentBookingForm(specialities, rooms);
        } catch (ReadSpecialityException | ReadRoomException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void showAppointmentList() {
        try {
            List<Appointment> appointments = appointmentManager.getAllAppointments();
            mainView.showAppointmentList(appointments);
        } catch (ReadAppointmentException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void showAppointmentsForMember(int memberId) {
        try {
            List<Appointment> appointments = appointmentManager.getAppointmentsByMemberId(memberId);
            mainView.showAppointmentList(appointments);

        } catch (Exception exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void showAppointmentsForCoach(int coachId) {
        try {
            List<Appointment> appointments = appointmentManager.getAppointmentsByCoachId(coachId);
            mainView.showAppointmentList(appointments);

        } catch (Exception exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void searchAvailableSlots(String specialityName, LocalDate startDate, LocalDate endDate) {
        try {
            List<AvailableCoachSearchResult> results =
                    searchManager.searchAvailableCoachesBySpecialityAndDateRange(
                            specialityName,
                            startDate,
                            endDate
                    );
            mainView.showAppointmentBookingSlots(results);
        } catch (SearchException exception) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }

    public void bookAppointment(
            int memberId,
            int availabilityId,
            String specialityName,
            Integer roomId,
            String objective
    ) {
        try {
            appointmentManager.bookAppointment(
                    memberId,
                    availabilityId,
                    specialityName,
                    roomId,
                    objective
            );
            mainView.showInformationMessage("Rendez-vous reserve avec succes.");
            showBookingForm();
        } catch (
                ReadGymMemberException |
                ReadCoachAvailabilityException |
                ReadRoomException |
                ReadAppointmentException |
                AddAppointmentException |
                UpdateCoachAvailabilityException |
                AppointmentBusinessException exception
        ) {
            mainView.showErrorMessage(exception.getMessage());
        }
    }
}
