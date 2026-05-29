package modelPackage;

public class Qualification {
    private Coach coach;
    private Speciality specialityName;

    public Qualification(Coach coach, Speciality specialityName) {
        setCoach(coach);
        setSpecialityName(specialityName);
    }

    public Coach getCoach() {
        return coach;
    }

    public Speciality getSpecialityName() {
        return specialityName;
    }

    public void setCoach(Coach coach) {
        if (coach == null) {
            throw new IllegalArgumentException("Le coach ne peut pas être null");
        }
        this.coach = coach;
    }

    public void setSpecialityName(Speciality specialityName) {
        if (specialityName == null) {
            throw new IllegalArgumentException("La spécialité ne peut pas être null");
        }
        this.specialityName = specialityName;
    }
}