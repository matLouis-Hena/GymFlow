package modelPackage;

import java.time.LocalDate;

public class Sponsorship {
    private int id;
    private LocalDate startDate;
    private GymMember sponsor;
    private GymMember sponsored;

    public Sponsorship(int id, LocalDate startDate, GymMember sponsor, GymMember sponsored) {
        this.id = id;
        setStartDate(startDate);
        setSponsor(sponsor);
        setSponsored(sponsored);
    }

    public int getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public GymMember getSponsor() {
        return sponsor;
    }

    public GymMember getSponsored() {
        return sponsored;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("La date de début du parrainage ne peut pas être null");
        }

        this.startDate = startDate;
    }

    public void setSponsor(GymMember sponsor) {
        if (sponsor == null) {
            throw new IllegalArgumentException("Le parrain ne peut pas être null");
        }

        this.sponsor = sponsor;
    }

    public void setSponsored(GymMember sponsored) {
        if (sponsored == null) {
            throw new IllegalArgumentException("Le filleul ne peut pas être null");
        }

        this.sponsored = sponsored;
    }
}