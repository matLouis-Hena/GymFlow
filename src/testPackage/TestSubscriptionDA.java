package testPackage;

import dataAccessPackage.subscriptionDataAccess.ISubscriptionDA;
import dataAccessPackage.subscriptionDataAccess.SubscriptionDBAccess;
import exceptionPackage.subscription.ReadSubscriptionException;
import modelPackage.Subscription;

import java.util.List;

public class TestSubscriptionDA {

    public static void main(String[] args) {
        ISubscriptionDA subscriptionDataAccess = new SubscriptionDBAccess();

        try {
            List<Subscription> subscriptions = subscriptionDataAccess.getAll();

            System.out.println("Abonnements récupérés : " + subscriptions.size());

            for (Subscription subscription : subscriptions) {
                System.out.println(
                        subscription.getId()
                                + " - tier "
                                + subscription.getType()
                                + " - "
                                + subscription.getPrice()
                                + "€ - "
                                + subscription.getDurationMonths()
                                + " mois"
                );
            }

            Subscription subscription = subscriptionDataAccess.getById(3);

            if (subscription != null) {
                System.out.println(
                        "Abonnement récupéré : "
                                + subscription.getId()
                                + " - tier "
                                + subscription.getType()
                );
            } else {
                System.out.println("Aucun abonnement trouvé avec l'id 1.");
            }

        } catch (ReadSubscriptionException exception) {
            System.out.println("Erreur pendant le test SubscriptionDA : " + exception.getMessage());
        }
    }
}