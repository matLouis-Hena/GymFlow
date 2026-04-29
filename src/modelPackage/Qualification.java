package modelPackage;

public class Qualification {
    private Coach coach;
    private Speciality specialityName;

    public Qualification(Coach coach, Speciality specialityName) {
        this.coach = coach;
        this.specialityName = specialityName;
    }

    // Setters
    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public void setSpecialityName(Speciality specialityName) {
        this.specialityName = specialityName;
    }
}