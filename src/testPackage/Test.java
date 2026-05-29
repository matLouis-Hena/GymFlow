package testPackage;

import businessPackage.GymMemberManager;

public class Test {

    public static void main(String[] args) {
        GymMemberManager gymMemberManager = new GymMemberManager();

        try {
            int memberId = 5;

            gymMemberManager.deleteMemberWithDependencies(memberId);

            System.out.println("Membre supprimé avec toutes ses dépendances.");

        } catch (Exception exception) {
            System.out.println("Erreur pendant la suppression complète : " + exception.getMessage());
        }
    }
}