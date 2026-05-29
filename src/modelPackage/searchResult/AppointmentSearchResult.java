package modelPackage.searchResult;

import modelPackage.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentSearchResult {

    private int appointmentId;
    private int memberId;
    private String memberFullName;
    private String coachFullName;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String roomName;
    private String objective;
    private AppointmentStatus status;

    public AppointmentSearchResult(
            int appointmentId,
            int memberId,
            String memberFullName,
            String coachFullName,
            LocalDate appointmentDate,
            LocalTime startTime,
            LocalTime endTime,
            String roomName,
            String objective,
            AppointmentStatus status
    ) {
        this.appointmentId = appointmentId;
        this.memberId = memberId;
        this.memberFullName = memberFullName;
        this.coachFullName = coachFullName;
        this.appointmentDate = appointmentDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomName = roomName;
        this.objective = objective;
        this.status = status;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getMemberFullName() {
        return memberFullName;
    }

    public String getCoachFullName() {
        return coachFullName;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getObjective() {
        return objective;
    }

    public AppointmentStatus getStatus() {
        return status;
    }
}