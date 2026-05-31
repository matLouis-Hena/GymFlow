package modelPackage.searchResult;

import java.time.LocalDate;
import java.time.LocalTime;

public class AvailableCoachSearchResult {

    private int availabilityId;
    private int coachId;
    private String coachFirstName;
    private String coachLastName;
    private boolean hasDegree;
    private LocalDate availableDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public AvailableCoachSearchResult(
            int availabilityId,
            int coachId,
            String coachFirstName,
            String coachLastName,
            boolean hasDegree,
            LocalDate availableDate,
            LocalTime startTime,
            LocalTime endTime
    ) {
        this.availabilityId = availabilityId;
        this.coachId = coachId;
        this.coachFirstName = coachFirstName;
        this.coachLastName = coachLastName;
        this.hasDegree = hasDegree;
        this.availableDate = availableDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getAvailabilityId() {
        return availabilityId;
    }

    public int getCoachId() {
        return coachId;
    }

    public String getCoachFirstName() {
        return coachFirstName;
    }

    public String getCoachLastName() {
        return coachLastName;
    }

    public boolean getHasDegree() {
        return hasDegree;
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
}