package businessPackage;

import dataAccessPackage.subscriptionDataAccess.ISubscriptionDA;
import dataAccessPackage.subscriptionDataAccess.SubscriptionDBAccess;
import exceptionPackage.subscription.ReadSubscriptionException;
import modelPackage.Subscription;

import java.util.List;

public class SubscriptionManager {

    private final ISubscriptionDA subscriptionDataAccess;

    public SubscriptionManager() {
        this.subscriptionDataAccess = new SubscriptionDBAccess();
    }

    public SubscriptionManager(ISubscriptionDA subscriptionDataAccess) {
        this.subscriptionDataAccess = subscriptionDataAccess;
    }

    public List<Subscription> getAllSubscriptions() throws ReadSubscriptionException {
        return subscriptionDataAccess.getAll();
    }

    public Subscription getSubscriptionById(int id) throws ReadSubscriptionException {
        validateId(id);

        Subscription subscription = subscriptionDataAccess.getById(id);

        if (subscription == null) {
            throw new ReadSubscriptionException(
                    String.valueOf(id),
                    "Aucun abonnement trouvé avec cet identifiant."
            );
        }

        return subscription;
    }

    private void validateId(int id) throws ReadSubscriptionException {
        if (id <= 0) {
            throw new ReadSubscriptionException(
                    String.valueOf(id),
                    "L'identifiant de l'abonnement doit être supérieur à 0."
            );
        }
    }
}