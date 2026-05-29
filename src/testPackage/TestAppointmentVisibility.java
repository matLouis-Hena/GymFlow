package testPackage;

import businessPackage.AppointmentManager;
import modelPackage.Appointment;

import java.util.List;

public class TestAppointmentVisibility {

    public static void main(String[] args) {
        AppointmentManager appointmentManager = new AppointmentManager();

        try {
            int memberId = 7;
            int coachId = 4;

            List<Appointment> memberAppointments =
                    appointmentManager.getAppointmentsByMemberId(memberId);

            System.out.println("Rendez-vous du membre " + memberId + " : " + memberAppointments.size());

            for (Appointment appointment : memberAppointments) {
                System.out.println(
                        appointment.getId()
                                + " - "
                                + appointment.getAvailability().getAvailableDate()
                                + " - coach : "
                                + appointment.getAvailability().getCoach().getFirstName()
                                + " "
                                + appointment.getAvailability().getCoach().getLastName()
                );
            }

            List<Appointment> coachAppointments =
                    appointmentManager.getAppointmentsByCoachId(coachId);

            System.out.println("Rendez-vous du coach " + coachId + " : " + coachAppointments.size());

            for (Appointment appointment : coachAppointments) {
                System.out.println(
                        appointment.getId()
                                + " - "
                                + appointment.getAvailability().getAvailableDate()
                                + " - membre : "
                                + appointment.getMember().getFirstName()
                                + " "
                                + appointment.getMember().getLastName()
                );
            }

        } catch (Exception exception) {
            System.out.println("Erreur pendant le test AppointmentVisibility : " + exception.getMessage());
        }
    }
}