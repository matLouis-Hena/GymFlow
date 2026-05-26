package modelPackage;

import java.time.LocalDate;

public class Payment {
    private int id;
    private double amount;
    private LocalDate datePayment;
    private GymMember billing;

    public Payment(int id, double amount, LocalDate datePayment, GymMember billing) {
        this.id = id;
        setAmount(amount);
        this.datePayment = LocalDate.now();
        setBilling(billing);
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDatePayment() {
        return datePayment;
    }

    public GymMember getBilling() {
        return billing;
    }

    public void setAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0");
        }
        this.amount = amount;
    }

    public void setBilling(GymMember billing) {
        if (billing == null) {
            throw new IllegalArgumentException("Le membre ne peut pas être null");
        }
        this.billing = billing;
    }
}