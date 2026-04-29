package modelPackage;

import java.sql.Time;
import java.time.LocalDate;

public class CoachAvailability {
    private int id;
    private LocalDate availableDate;
    private Time startTime;
    private Time endTime;
    private Boolean isBooked;
    private Coach coach;

    public CoachAvailability(int id, LocalDate availableDate, Time startTime, Time endTime, Boolean isBooked, Coach coach) {
        this.id = id;
        this.availableDate = availableDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isBooked = isBooked;
        this.coach = coach;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setAvailableDate(LocalDate availableDate) {
        this.availableDate = availableDate;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public void setIsBooked(Boolean isBooked) {
        this.isBooked = isBooked;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }
}