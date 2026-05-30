package dataAccessPackage.appointmentDataAccess;

import exceptionPackage.appointment.AddAppointmentException;
import exceptionPackage.appointment.DeleteAppointmentException;
import exceptionPackage.appointment.ReadAppointmentException;
import exceptionPackage.appointment.UpdateAppointmentException;
import modelPackage.Appointment;

import java.time.LocalDate;
import java.util.List;

public interface IAppointmentDA {

    int insert(Appointment appointment) throws AddAppointmentException;

    Appointment getById(int id) throws ReadAppointmentException;

    List<Appointment> getAll() throws ReadAppointmentException;

    List<Appointment> getByMemberId(int memberId) throws ReadAppointmentException;

    List<Appointment> getByCoachId(int coachId) throws ReadAppointmentException;

    boolean existsForMemberOnDate(int memberId, LocalDate date) throws ReadAppointmentException;

    void updatePastConfirmedAppointments() throws UpdateAppointmentException;

    void updateStatus(int id, int status, String cancellationReason)
            throws UpdateAppointmentException;

    void delete(int id) throws DeleteAppointmentException;

    void deleteByMemberId(int memberId) throws DeleteAppointmentException;
}
