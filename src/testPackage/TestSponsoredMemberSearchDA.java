package testPackage;

import businessPackage.SearchManager;
import dataAccessPackage.gymMemberDataAccess.GymMemberDBAccess;
import dataAccessPackage.gymMemberDataAccess.IGymMemberDA;
import modelPackage.GymMember;
import modelPackage.searchResult.SponsoredMemberSearchResult;

import java.util.List;

public class TestSponsoredMemberSearchDA {

    public static void main(String[] args) {
        SearchManager searchManager = new SearchManager();
        IGymMemberDA gymMemberDataAccess = new GymMemberDBAccess();

        try {
            List<GymMember> members = gymMemberDataAccess.getAll();

            if (members.isEmpty()) {
                System.out.println("Aucun membre disponible pour tester la recherche de parrainage.");
                return;
            }

            GymMember sponsor = members.get(0);

            List<SponsoredMemberSearchResult> results =
                    searchManager.searchSponsoredMembersBySponsorId(sponsor.getId());

            System.out.println(
                    "Membres parrainés par "
                            + sponsor.getFirstName()
                            + " "
                            + sponsor.getLastName()
                            + " : "
                            + results.size()
            );

            for (SponsoredMemberSearchResult result : results) {
                System.out.println(
                        result.getSponsoredMemberId()
                                + " - "
                                + result.getFirstName()
                                + " "
                                + result.getLastName()
                                + " - "
                                + result.getEmail()
                                + " - veut casier : "
                                + result.getWantsLocker()
                                + " - "
                                + result.getWeight()
                                + "kg"
                                + " - "
                                + result.getHeight()
                                + "cm"
                                + " - abonnement : "
                                + result.getSubscriptionType().getDisplayValue()
                                + " - "
                                + result.getSubscriptionPrice()
                                + "€ / "
                                + result.getSubscriptionDurationMonths()
                                + " mois"
                );
            }

        } catch (Exception exception) {
            System.out.println("Erreur pendant le test SponsoredMemberSearchDA : " + exception.getMessage());
        }
    }
}
