package businessPackage;

import dataAccessPackage.paymentDataAccess.IPaymentDA;
import dataAccessPackage.paymentDataAccess.PaymentDBAccess;
import exceptionPackage.payment.DeletePaymentException;
import exceptionPackage.payment.ReadPaymentException;
import modelPackage.Payment;

import java.util.List;

public class PaymentManager {

    private final IPaymentDA paymentDataAccess;

    public PaymentManager() {
        this.paymentDataAccess = new PaymentDBAccess();
    }

    public PaymentManager(IPaymentDA paymentDataAccess) {
        this.paymentDataAccess = paymentDataAccess;
    }

    public List<Payment> getPaymentsByMemberId(int memberId) throws ReadPaymentException {
        validateMemberIdForRead(memberId);
        return paymentDataAccess.getByMemberId(memberId);
    }

    public void deletePaymentsByMemberId(int memberId) throws DeletePaymentException {
        validateMemberIdForDelete(memberId);
        paymentDataAccess.deleteByMemberId(memberId);
    }

    private void validateMemberIdForRead(int memberId) throws ReadPaymentException {
        if (memberId <= 0) {
            throw new ReadPaymentException(
                    String.valueOf(memberId),
                    "L'identifiant du membre doit être supérieur à 0."
            );
        }
    }

    private void validateMemberIdForDelete(int memberId) throws DeletePaymentException {
        if (memberId <= 0) {
            throw new DeletePaymentException(
                    String.valueOf(memberId),
                    "L'identifiant du membre doit être supérieur à 0."
            );
        }
    }
}