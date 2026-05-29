package testPackage;

import businessPackage.SearchManager;
import dataAccessPackage.gymMemberDataAccess.GymMemberDBAccess;
import dataAccessPackage.gymMemberDataAccess.IGymMemberDA;
import modelPackage.GymMember;
import modelPackage.searchResult.PaymentSearchResult;

import java.time.LocalDate;
import java.util.List;

public class TestPaymentSearchDA {

    public static void main(String[] args) {
        SearchManager searchManager = new SearchManager();
        IGymMemberDA gymMemberDataAccess = new GymMemberDBAccess();

        try {
            List<GymMember> members = gymMemberDataAccess.getAll();

            if (members.isEmpty()) {
                System.out.println("Aucun membre disponible pour tester la recherche de paiements.");
                return;
            }

            GymMember member = members.get(0);

            List<PaymentSearchResult> results =
                    searchManager.searchPaymentsByMemberAndDateRange(
                            member.getId(),
                            LocalDate.now().minusMonths(3),
                            LocalDate.now().plusDays(1)
                    );

            System.out.println(
                    "Paiements trouvés pour "
                            + member.getFirstName()
                            + " "
                            + member.getLastName()
                            + " : "
                            + results.size()
            );

            for (PaymentSearchResult result : results) {
                System.out.println(
                        result.getPaymentId()
                                + " - "
                                + result.getPaymentDate()
                                + " - "
                                + result.getAmount()
                                + "€ - "
                                + result.getSubscriptionType().getDisplayValue()
                                + " / "
                                + result.getSubscriptionDurationMonths()
                                + " mois"
                );
            }

        } catch (Exception exception) {
            System.out.println("Erreur pendant le test PaymentSearchDA : " + exception.getMessage());
        }
    }
}