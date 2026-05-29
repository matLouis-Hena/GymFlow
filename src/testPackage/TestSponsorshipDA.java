package testPackage;

import businessPackage.SponsorshipManager;
import dataAccessPackage.gymMemberDataAccess.GymMemberDBAccess;
import dataAccessPackage.gymMemberDataAccess.IGymMemberDA;
import modelPackage.GymMember;
import modelPackage.Sponsorship;

import java.util.List;

public class TestSponsorshipDA {

    public static void main(String[] args) {
        SponsorshipManager sponsorshipManager = new SponsorshipManager();
        IGymMemberDA gymMemberDataAccess = new GymMemberDBAccess();

        try {
            List<GymMember> members = gymMemberDataAccess.getAll();

            if (members.size() < 2) {
                System.out.println("Il faut au moins 2 membres pour tester SponsorshipDA.");
                return;
            }

            GymMember firstMember = members.get(0);
            GymMember secondMember = members.get(1);

            System.out.println(
                    "Membre test 1 : "
                            + firstMember.getId()
                            + " - "
                            + firstMember.getFirstName()
                            + " "
                            + firstMember.getLastName()
            );

            System.out.println(
                    "Membre test 2 : "
                            + secondMember.getId()
                            + " - "
                            + secondMember.getFirstName()
                            + " "
                            + secondMember.getLastName()
            );

            List<Sponsorship> sponsorships =
                    sponsorshipManager.getSponsorshipsByMemberId(firstMember.getId());

            System.out.println(
                    "Parrainages liés au membre "
                            + firstMember.getId()
                            + " : "
                            + sponsorships.size()
            );

            for (Sponsorship sponsorship : sponsorships) {
                System.out.println(
                        sponsorship.getId()
                                + " - "
                                + sponsorship.getStartDate()
                                + " - parrain : "
                                + sponsorship.getSponsor().getFirstName()
                                + " "
                                + sponsorship.getSponsor().getLastName()
                                + " - filleul : "
                                + sponsorship.getSponsored().getFirstName()
                                + " "
                                + sponsorship.getSponsored().getLastName()
                );
            }

            int sponsoredCount = sponsorshipManager.countSponsoredBySponsorId(firstMember.getId());

            System.out.println(
                    "Nombre de filleuls du membre "
                            + firstMember.getId()
                            + " : "
                            + sponsoredCount
            );

            boolean secondMemberAlreadySponsored =
                    sponsorshipManager.existsForSponsoredId(secondMember.getId());

            System.out.println(
                    "Le membre "
                            + secondMember.getId()
                            + " a déjà un parrain : "
                            + secondMemberAlreadySponsored
            );

        } catch (Exception exception) {
            System.out.println("Erreur pendant le test SponsorshipDA : " + exception.getMessage());
        }
    }
}