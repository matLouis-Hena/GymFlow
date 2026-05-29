package modelPackage;

import java.sql.Time;
import java.time.LocalDate;

import exceptionPackage.validation.InvalidDateException;
import exceptionPackage.validation.InvalidTimeException;

public class CoachAvailability {
    private int id;
    private LocalDate availableDate;
    private Time startTime;
    private Time endTime;
    private boolean isBooked;
    private Coach coach;

    public CoachAvailability(int id, LocalDate availableDate, Time startTime, Time endTime, boolean isBooked, Coach coach)
            throws InvalidTimeException, InvalidDateException {
        this.id = id;
        setAvailableDate(availableDate);
        setStartTime(startTime);
        setEndTime(endTime);
        this.isBooked = isBooked;
        setCoach(coach);
    }

    public int getId() {
        return id;
    }

    public LocalDate getAvailableDate() {
        return availableDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setAvailableDate(LocalDate availableDate) throws InvalidDateException {
        if (availableDate != null && availableDate.isBefore(LocalDate.now())) {
            throw new InvalidDateException(availableDate, "La date de disponibilité ne peut pas être dans le passé");
        }
        this.availableDate = availableDate;
    }

    public void setStartTime(Time startTime) throws InvalidTimeException {
        if (startTime == null) {
            throw new InvalidTimeException(null, "L'heure de début ne peut pas être nulle");
        }
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) throws InvalidTimeException {
        if (this.startTime != null && endTime != null && !endTime.after(this.startTime)) {
            throw new InvalidTimeException(endTime, "L'heure de fin doit être après l'heure de début");
        }
        this.endTime = endTime;
    }

    public void setIsBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }

    public void setCoach(Coach coach) {
        if (coach == null) {
            throw new IllegalArgumentException("Le coach ne peut pas être null");
        }
        this.coach = coach;
    }
}