package testPackage;

import businessPackage.GymMemberManager;
import modelPackage.Gender;
import modelPackage.GymMember;
import modelPackage.Subscription;
import modelPackage.SubscriptionType;

import java.time.LocalDate;
import java.util.List;

public class TestGymMemberService {

    public static void main(String[] args) {
        GymMemberManager manager = new GymMemberManager();
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
                    "matgym@test.com",
                    null,
                    null,
                    "matgym",
                    "password123",
                    true,
                    72.5,
                    180,
                    subscription
            );

            manager.registerMember(member);
            System.out.println("Membre inséré");

            List<GymMember> members = manager.getAllMembers();
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

            GymMember retrievedMember = manager.getMemberById(insertedMemberId);

            System.out.println(
                    "Membre récupéré : "
                            + retrievedMember.getFirstName()
                            + " "
                            + retrievedMember.getLastName()
            );

            retrievedMember.setWeight(80.0);
            manager.updateMember(retrievedMember);

            GymMember memberAfterUpdate = manager.getMemberById(insertedMemberId);

            System.out.println(
                    "Après update : "
                            + memberAfterUpdate.getWeight()
                            + "kg"
            );

        } catch (Exception exception) {
            System.out.println("Erreur pendant le test : " + exception.getMessage());
            cleanInsertedMember(manager, insertedMemberId);
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

    private static void cleanInsertedMember(GymMemberManager manager, int insertedMemberId) {
        if (insertedMemberId <= 0) {
            return;
        }

        try {
            manager.deleteMember(insertedMemberId);
            System.out.println("Nettoyage : membre supprimé après erreur.");
        } catch (Exception exception) {
            System.out.println("Nettoyage impossible : " + exception.getMessage());
        }
    }
}