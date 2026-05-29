package modelPackage.searchResult;

import modelPackage.SubscriptionType;

import java.time.LocalDate;

public class PaymentSearchResult {

    private int paymentId;
    private int memberId;
    private String memberFullName;
    private double amount;
    private LocalDate paymentDate;
    private SubscriptionType subscriptionType;
    private int subscriptionDurationMonths;

    public PaymentSearchResult(
            int paymentId,
            int memberId,
            String memberFullName,
            double amount,
            LocalDate paymentDate,
            SubscriptionType subscriptionType,
            int subscriptionDurationMonths
    ) {
        this.paymentId = paymentId;
        this.memberId = memberId;
        this.memberFullName = memberFullName;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.subscriptionType = subscriptionType;
        this.subscriptionDurationMonths = subscriptionDurationMonths;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getMemberFullName() {
        return memberFullName;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public int getSubscriptionDurationMonths() {
        return subscriptionDurationMonths;
    }
}