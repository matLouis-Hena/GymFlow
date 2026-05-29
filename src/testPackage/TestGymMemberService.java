package testPackage;

import businessPackage.GymMemberManager;
import modelPackage.Gender;
import modelPackage.GymMember;
import modelPackage.Subscription;

import java.time.LocalDate;
import java.util.List;

public class TestGymMemberService {

    public static void main(String[] args) {
        GymMemberManager service = new GymMemberManager();
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

            service.registerMember(member);
            System.out.println("Membre inséré");

            List<GymMember> members = service.getAllMembers();

            GymMember insertedMember = findMemberByUsername(
                    members,
                    member.getUsername()
            );

            insertedMemberId = insertedMember.getId();

            System.out.println(
                    insertedMember.getFirstName()
                            + " "
                            + insertedMember.getLastName()
                            + " - "
                            + insertedMember.getWeight()
                            + "kg"
            );

            GymMember retrievedMember = service.getMemberById(insertedMemberId);

            System.out.println(
                    "Membre récupéré : "
                            + retrievedMember.getFirstName()
                            + " "
                            + retrievedMember.getLastName()
            );

            GymMember updatedMember = new GymMember(
                    insertedMemberId,
                    retrievedMember.getFirstName(),
                    retrievedMember.getLastName(),
                    retrievedMember.getBirthDate(),
                    retrievedMember.getGender(),
                    retrievedMember.getEmail(),
                    retrievedMember.getPhone(),
                    retrievedMember.getLockerNumber(),
                    retrievedMember.getUsername(),
                    retrievedMember.getPassword(),
                    retrievedMember.getIsActive(),
                    80.0,
                    retrievedMember.getHeight(),
                    retrievedMember.getEnrollment()
            );

            service.updateMember(updatedMember);

            GymMember memberAfterUpdate = service.getMemberById(insertedMemberId);

            System.out.println(
                    "Après update : "
                            + memberAfterUpdate.getWeight()
                            + "kg"
            );

            //service.deleteMember(insertedMemberId);
            //insertedMemberId = -1;

            //System.out.println("Membre supprimé");

        } catch (Exception exception) {
            System.out.println("Erreur pendant le test : " + exception.getMessage());
            exception.printStackTrace();

            cleanInsertedMember(service, insertedMemberId);
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

    private static void cleanInsertedMember(GymMemberManager service, int insertedMemberId) {
        if (insertedMemberId <= 0) {
            return;
        }

        try {
            service.deleteMember(insertedMemberId);
            System.out.println("Nettoyage : membre supprimé après erreur.");
        } catch (Exception cleanupException) {
            System.out.println("Nettoyage impossible : " + cleanupException.getMessage());
        }
    }
}