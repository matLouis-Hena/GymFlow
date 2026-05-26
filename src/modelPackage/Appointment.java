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
        setMember(member);
        setAvailability(availability);
        this.objective = objective;
        this.room = room;
        setStatus(status);
        setCancellationReason(cancellationReason);
    }

    public int getId() {
        return id;
    }

    public GymMember getMember() {
        return member;
    }

    public CoachAvailability getAvailability() {
        return availability;
    }

    public String getObjective() {
        return objective;
    }

    public Room getRoom() {
        return room;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setMember(GymMember member) {
        if (member == null) {
            throw new IllegalArgumentException("Le membre ne peut pas être null");
        }
        this.member = member;
    }

    public void setAvailability(CoachAvailability availability) {
        if (availability == null) {
            throw new IllegalArgumentException("La disponibilité ne peut pas être null");
        }
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
        if ((this.status == AppointmentStatus.CANCELLED_BY_COACH
                || this.status == AppointmentStatus.CANCELLED_BY_MEMBER)
                && (cancellationReason == null || cancellationReason.trim().isEmpty())) {
            throw new IllegalArgumentException("Une raison d'annulation est obligatoire si le rendez-vous est annulé");
        }
        this.cancellationReason = cancellationReason;
    }
}