package modelPackage;

public class Access {
    private Subscription subscription;
    private Facility facility;

    public Access(Subscription subscription, Facility facility) {
        setSubscription(subscription);
        setFacility(facility);
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setSubscription(Subscription subscription) {
        if (subscription == null) {
            throw new IllegalArgumentException("L'abonnement ne peut pas être null");
        }
        this.subscription = subscription;
    }

    public void setFacility(Facility facility) {
        if (facility == null) {
            throw new IllegalArgumentException("L'équipement ne peut pas être null");
        }
        this.facility = facility;
    }

}