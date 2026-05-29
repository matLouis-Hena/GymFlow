package testPackage;

import businessPackage.PaymentManager;
import dataAccessPackage.gymMemberDataAccess.*;
import modelPackage.GymMember;
import modelPackage.Payment;

import java.util.List;

public class TestPaymentDA {

    public static void main(String[] args) {
        PaymentManager paymentManager = new PaymentManager();
        IGymMemberDA gymMemberDataAccess = new GymMemberDBAccess();

        try {
            List<GymMember> members = gymMemberDataAccess.getAll();

            if (members.isEmpty()) {
                System.out.println("Aucun membre disponible pour tester PaymentDA.");
                return;
            }

            GymMember member = members.get(0);

            List<Payment> payments = paymentManager.getPaymentsByMemberId(member.getId());

            System.out.println(
                    "Paiements récupérés pour "
                            + member.getFirstName()
                            + " "
                            + member.getLastName()
                            + " : "
                            + payments.size()
            );

            for (Payment payment : payments) {
                System.out.println(
                        payment.getId()
                                + " - "
                                + payment.getDatePayment()
                                + " - "
                                + payment.getAmount()
                                + "€"
                );
            }

        } catch (Exception exception) {
            System.out.println("Erreur pendant le test PaymentDA : " + exception.getMessage());
        }
    }
}