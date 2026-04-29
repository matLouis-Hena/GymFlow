package modelPackage;

import java.time.LocalDate;

public class Payment {
    private int id;
    private double amount;
    private LocalDate datePayment;
    private GymMember billing;

    public Payment(int id, double amount, LocalDate datePayment, GymMember billing) {
        this.id = id;
        this.amount = amount;
        this.datePayment = datePayment;
        this.billing = billing;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDatePayment(LocalDate datePayment) {
        this.datePayment = datePayment;
    }

    public void setBilling(GymMember billing) {
        this.billing = billing;
    }
}