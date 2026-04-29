package modelPackage;

public class Appointment {
    private int id;
    private GymMember member;
    private CoachAvailability availability;
    private String objective;
    private Room room;
    private AppointmentStatus status;
    private String cancellationReason;

    public Appointment(int id, GymMember member, CoachAvailability availability, String objective, Room room, AppointmentStatus status, String cancellationReason) {
        this.id = id;
        this.member = member;
        this.availability = availability;
        this.objective = objective;
        this.room = room;
        setStatus(status);
        this.cancellationReason = cancellationReason;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setMember(GymMember member) {
        this.member = member;
    }

    public void setAvailability(CoachAvailability availability) {
        this.availability = availability;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setStatus(AppointmentStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Le statut du rendez-vous ne peut pas être null");
        }
        this.status = status;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
}