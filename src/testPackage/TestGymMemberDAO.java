package testPackage;

import dataAccessPackage.gymMemberDataAccess.IGymMemberDA;
import dataAccessPackage.gymMemberDataAccess.GymMemberDBAccess;
import exceptionPackage.gymMember.DeleteGymMemberException;
import modelPackage.Gender;
import modelPackage.GymMember;
import modelPackage.Subscription;

import java.time.LocalDate;
import java.util.List;

public class TestGymMemberDAO {

    public static void main(String[] args) {
        IGymMemberDA dao = new GymMemberDBAccess();
        int insertedMemberId = -1;

        try {
            Subscription subscription = new Subscription(
                    1,
                    1,
                    29.99,
                    1
            );

            GymMember member = new GymMember(
                    0,
                    "Mat",
                    "Gym",
                    LocalDate.of(2000, 1, 1),
                    Gender.MALE,
                    "matgym@tests.com",
                    null,
                    null,
                    "matgyms",
                    "password123",
                    true,
                    72.5,
                    180,
                    subscription
            );

            dao.insert(member);
            System.out.println("Membre inséré");

            List<GymMember> members = dao.getAll();
            GymMember insertedMember = findMemberByUsername(members, member.getUsername());
            insertedMemberId = insertedMember.getId();

            System.out.println(
                    insertedMember.getFirstName()
                            + " "
                            + insertedMember.getLastName()
                            + " - "
                            + insertedMember.getWeight()
                            + "kg"
            );

            GymMember retrievedMember = dao.getById(insertedMemberId);

            retrievedMember.setWeight(60.0);
            dao.update(retrievedMember);

            GymMember updatedMember = dao.getById(insertedMemberId);

            System.out.println(
                    "Membre mis à jour : "
                            + updatedMember.getWeight()
                            + "kg"
            );
//
            //dao.delete(insertedMemberId);
            //insertedMemberId = -1;
//
            //System.out.println("Membre supprimé");

        } catch (Exception exception) {
            System.out.println("Erreur pendant le test DAO : " + exception.getMessage());
            cleanInsertedMember(dao, insertedMemberId);
        }
    }

    private static GymMember findMemberByUsername(List<GymMember> members, String username) {
        for (GymMember member : members) {
            if (member.getUsername().equals(username)) {
                return member;
            }
        }

        throw new IllegalStateException("Le membre inséré n'a pas été retrouvé.");
    }

    private static void cleanInsertedMember(IGymMemberDA dao, int insertedMemberId) {
        if (insertedMemberId <= 0) {
            return;
        }

        try {
            dao.delete(insertedMemberId);
            System.out.println("Nettoyage : membre supprimé après erreur.");
        } catch (DeleteGymMemberException exception) {
            System.out.println("Nettoyage impossible : " + exception.getMessage());
        }
    }
}