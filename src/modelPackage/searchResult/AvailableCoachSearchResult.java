package modelPackage.searchResult;

import java.time.LocalDate;
import java.time.LocalTime;

public class AvailableCoachSearchResult {

    private int availabilityId;
    private int coachId;
    private String coachFullName;
    private String specialityName;
    private LocalDate availableDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean hasDegree;

    public AvailableCoachSearchResult(
            int availabilityId,
            int coachId,
            String coachFullName,
            String specialityName,
            LocalDate availableDate,
            LocalTime startTime,
            LocalTime endTime,
            boolean hasDegree
    ) {
        this.availabilityId = availabilityId;
        this.coachId = coachId;
        this.coachFullName = coachFullName;
        this.specialityName = specialityName;
        this.availableDate = availableDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hasDegree = hasDegree;
    }

    public int getAvailabilityId() {
        return availabilityId;
    }

    public int getCoachId() {
        return coachId;
    }

    public String getCoachFullName() {
        return coachFullName;
    }

    public String getSpecialityName() {
        return specialityName;
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

    public boolean getHasDegree() {
        return hasDegree;
    }
}