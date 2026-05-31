package modelPackage;

import exceptionPackage.validation.InvalidDurationException;
import exceptionPackage.validation.InvalidPriceException;

public class Subscription {

    private int id;
    private SubscriptionType type;
    private double price;
    private int durationMonths;

    public Subscription(int id, SubscriptionType type, int durationMonths)
            throws InvalidDurationException, InvalidPriceException {
        this.id = id;
        setType(type);
        setDurationMonths(durationMonths);
        setPrice(type.calculatePrice(durationMonths));
    }

    public Subscription(int id, SubscriptionType type, double price, int durationMonths)
            throws InvalidDurationException, InvalidPriceException {
        this.id = id;
        setType(type);
        setDurationMonths(durationMonths);
        setPrice(price);
    }

    public int getId() {
        return id;
    }

    public SubscriptionType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setType(SubscriptionType type) {
        if (type == null) {
            throw new IllegalArgumentException("Subscription type cannot be null.");
        }

        this.type = type;
    }

    public void setDurationMonths(int durationMonths) throws InvalidDurationException {
        if (durationMonths <= 0) {
            throw new InvalidDurationException(durationMonths, "La durée de l'abonnement doit être supérieure à 0.");
        }

        this.durationMonths = durationMonths;
    }

    public void setPrice(double price) throws InvalidPriceException {
        if (price <= 0) {
            throw new InvalidPriceException(price, "Le prix de l'abonnement doit être supérieur à 0.");
        }

        double expectedPrice = type.calculatePrice(durationMonths);

        if (Math.abs(price - expectedPrice) > 0.01) {
            throw new InvalidPriceException(price, "Le prix de l'abonnement ne correspond pas au type et à la durée.");
        }

        this.price = price;
    }

    public String toString() {
        return type.getDisplayValue()
                + " - "
                + price
                + "€ / "
                + durationMonths
                + " mois";
    }
}