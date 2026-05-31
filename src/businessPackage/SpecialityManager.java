package businessPackage;

import dataAccessPackage.specialityDataAccess.ISpecialityDA;
import dataAccessPackage.specialityDataAccess.SpecialityDBAccess;
import exceptionPackage.speciality.ReadSpecialityException;
import modelPackage.Speciality;

import java.util.List;

public class SpecialityManager {

    private final ISpecialityDA specialityDataAccess;

    public SpecialityManager() {
        this.specialityDataAccess = new SpecialityDBAccess();
    }

    public SpecialityManager(ISpecialityDA specialityDataAccess) {
        this.specialityDataAccess = specialityDataAccess;
    }

    public List<Speciality> getAllSpecialities() throws ReadSpecialityException {
        return specialityDataAccess.getAll();
    }

    public Speciality getSpecialityByName(String name) throws ReadSpecialityException {
        validateName(name);

        Speciality speciality = specialityDataAccess.getByName(name);

        if (speciality == null) {
            throw new ReadSpecialityException(
                    name,
                    "Aucune spécialité trouvée avec ce nom."
            );
        }

        return speciality;
    }

    private void validateName(String name) throws ReadSpecialityException {
        if (name == null || name.isBlank()) {
            throw new ReadSpecialityException(
                    "name",
                    "Le nom de la spécialité est obligatoire."
            );
        }
    }
}