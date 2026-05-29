package testPackage;

import dataAccessPackage.gymMemberDataAccess.GymMemberDBAccess;
import dataAccessPackage.gymMemberDataAccess.IGymMemberDA;
import exceptionPackage.gymMember.DeleteGymMemberException;
import modelPackage.Gender;
import modelPackage.GymMember;
import modelPackage.Subscription;
import modelPackage.SubscriptionType;

import java.time.LocalDate;
import java.util.List;

public class TestGymMemberDAO {

    public static void main(String[] args) {
        IGymMemberDA dataAccess = new GymMemberDBAccess();
        int insertedMemberId = -1;

        try {
            Subscription subscription = new Subscription(
                    0,
                    SubscriptionType.BASIC,
                    1
            );

            GymMember member = new GymMember(
                    0,
                    "Mat",
                    "Gym",
                    LocalDate.of(2000, 1, 1),
                    Gender.MALE,
                    "matgyms@test.com",
                    null,
                    null,
                    "matgyms",
                    "password123",
                    true,
                    72.5,
                    180,
                    subscription
            );

            dataAccess.insert(member);
            System.out.println("Membre inséré");

            List<GymMember> members = dataAccess.getAll();
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

            GymMember retrievedMember = dataAccess.getById(insertedMemberId);

            System.out.println(
                    "Membre récupéré : "
                            + retrievedMember.getFirstName()
                            + " "
                            + retrievedMember.getLastName()
            );

            retrievedMember.setWeight(60.0);
            dataAccess.update(retrievedMember);

            GymMember updatedMember = dataAccess.getById(insertedMemberId);

            System.out.println(
                    "Membre mis à jour : "
                            + updatedMember.getWeight()
                            + "kg"
            );

        } catch (Exception exception) {
            System.out.println("Erreur pendant le test DAO : " + exception.getMessage());
            cleanInsertedMember(dataAccess, insertedMemberId);
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

    private static void cleanInsertedMember(IGymMemberDA dataAccess, int insertedMemberId) {
        if (insertedMemberId <= 0) {
            return;
        }

        try {
            dataAccess.delete(insertedMemberId);
            System.out.println("Nettoyage : membre supprimé après erreur.");
        } catch (DeleteGymMemberException exception) {
            System.out.println("Nettoyage impossible : " + exception.getMessage());
        }
    }
}