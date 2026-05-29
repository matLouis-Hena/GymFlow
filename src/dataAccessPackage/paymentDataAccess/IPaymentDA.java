package dataAccessPackage.paymentDataAccess;

import exceptionPackage.payment.DeletePaymentException;
import exceptionPackage.payment.ReadPaymentException;
import modelPackage.Payment;

import java.util.List;

public interface IPaymentDA {

    List<Payment> getByMemberId(int memberId) throws ReadPaymentException;

    void deleteByMemberId(int memberId) throws DeletePaymentException;
}