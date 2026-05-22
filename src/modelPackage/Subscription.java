package modelPackage;

import exceptionPackage.*;

public class Subscription {
    private int id;
    private int tier;
    private double price;
    private int durationMonths;

    public Subscription(int id, int tier, double price, int durationMonths)
            throws InvalidPriceException, InvalidDurationException {
        this.id = id;
        this.tier = tier;
        setPrice(price);
        setDurationMonths(durationMonths);
    }

    public void setPrice(double price) throws InvalidPriceException {
        if (price <= 0) {
            throw new InvalidPriceException(price, "Le prix doit être supérieur à 0");
        }
        this.price = price;
    }

    public void setDurationMonths(int durationMonths) throws InvalidDurationException {
        if (durationMonths <= 0) {
            throw new InvalidDurationException(durationMonths, "La durée doit être supérieure à 0");
        }
        this.durationMonths = durationMonths;
    }
}