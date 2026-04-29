package modelPackage;

import java.time.LocalDate;

public class Sponsorship {
    private int id;
    private LocalDate startDate;
    private GymMember sponsor;
    private GymMember sponsored;

    public Sponsorship(int id, LocalDate startDate, GymMember sponsor, GymMember sponsored) {
        this.id = id;
        this.startDate = startDate;
        this.sponsor = sponsor;
        this.sponsored = sponsored;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setSponsor(GymMember sponsor) {
        this.sponsor = sponsor;
    }

    public void setSponsored(GymMember sponsored) {
        this.sponsored = sponsored;
    }
}