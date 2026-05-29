package modelPackage.searchResult;

import modelPackage.SubscriptionType;

public class SponsoredMemberSearchResult {

    private int sponsoredMemberId;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isActive;
    private double weight;
    private int height;
    private SubscriptionType subscriptionType;
    private double subscriptionPrice;
    private int subscriptionDurationMonths;

    public SponsoredMemberSearchResult(
            int sponsoredMemberId,
            String firstName,
            String lastName,
            String email,
            boolean isActive,
            double weight,
            int height,
            SubscriptionType subscriptionType,
            double subscriptionPrice,
            int subscriptionDurationMonths
    ) {
        this.sponsoredMemberId = sponsoredMemberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isActive = isActive;
        this.weight = weight;
        this.height = height;
        this.subscriptionType = subscriptionType;
        this.subscriptionPrice = subscriptionPrice;
        this.subscriptionDurationMonths = subscriptionDurationMonths;
    }

    public int getSponsoredMemberId() {
        return sponsoredMemberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public double getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public double getSubscriptionPrice() {
        return subscriptionPrice;
    }

    public int getSubscriptionDurationMonths() {
        return subscriptionDurationMonths;
    }
}