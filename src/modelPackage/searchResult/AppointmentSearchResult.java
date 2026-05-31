package modelPackage.searchResult;

import modelPackage.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentSearchResult {

    private int appointmentId;
    private String coachFirstName;
    private String coachLastName;
    private LocalDate availableDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String objective;
    private AppointmentStatus status;
    private String roomName;

    public AppointmentSearchResult(
            int appointmentId,
            String coachFirstName,
            String coachLastName,
            LocalDate availableDate,
            LocalTime startTime,
            LocalTime endTime,
            String objective,
            AppointmentStatus status,
            String roomName
    ) {
        this.appointmentId = appointmentId;
        this.coachFirstName = coachFirstName;
        this.coachLastName = coachLastName;
        this.availableDate = availableDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.objective = objective;
        this.status = status;
        this.roomName = roomName;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public String getCoachFirstName() {
        return coachFirstName;
    }

    public String getCoachLastName() {
        return coachLastName;
    }

    public LocalDate getAvailableDate() {
        return availableDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getObjective() {
        return objective;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public String getRoomName() {
        return roomName;
    }
}