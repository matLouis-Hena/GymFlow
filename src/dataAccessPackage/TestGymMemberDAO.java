package dataAccessPackage;

import modelPackage.Gender;
import modelPackage.GymMember;

import java.time.LocalDate;
import java.util.List;

public class TestGymMemberDAO {

    public static void main(String[] args) {

        try {
            String unique = String.valueOf(System.currentTimeMillis());

            GymMember member = new GymMember(
                    0,
                    "Mat",
                    "Gym",
                    LocalDate.of(2000, 1, 1),
                    Gender.MALE,
                    "matgym" + unique + "@test.com",
                    null,
                    null,
                    "matgym" + unique,
                    "password123",

                    true,
                    72.5,
                    180,
                    null
            );

            GymMemberDAO dao = new GymMemberDAOImpl();

            // INSERT
            dao.insert(member);
            System.out.println("Membre inséré");

            // GET ALL
            List<GymMember> members = dao.getAll();

            for (GymMember m : members) {
                System.out.println(
                        m.getFirstName()
                                + " "
                                + m.getLastName()
                                + " - "
                                + m.getWeight()
                                + "kg"
                );
            }

            // GET BY ID
            GymMember retrieved = dao.getAll().getLast();

            System.out.println("Membre récupéré : "
                    + retrieved.getFirstName()
                    + " "
                    + retrieved.getLastName());

            // UPDATE
            retrieved.setWeight(80.0);
            dao.update(retrieved);

            GymMember updated = dao.getById(retrieved.getId());

            System.out.println("Après update : "
                    + updated.getWeight()
                    + "kg");

            // DELETE
            dao.delete(updated.getId());

            System.out.println("Membre supprimé");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}