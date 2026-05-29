package dataAccessPackage.paymentDataAccess;

import dataAccessPackage.SingletonConnection;
import dataAccessPackage.gymMemberDataAccess.*;
import exceptionPackage.gymMember.ReadGymMemberException;
import exceptionPackage.payment.*;
import modelPackage.GymMember;
import modelPackage.Payment;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaymentDBAccess implements IPaymentDA {

    private static final String SELECT_PAYMENTS_BY_MEMBER_ID_SQL = """
            SELECT id, amount, date_payment, billing
            FROM payment
            WHERE billing = ?
            ORDER BY date_payment DESC, id DESC
            """;

    private static final String DELETE_PAYMENTS_BY_MEMBER_ID_SQL = """
            DELETE FROM payment
            WHERE billing = ?
            """;

    private final IGymMemberDA gymMemberDataAccess;

    private Connection connection;

    public PaymentDBAccess() {
        this.gymMemberDataAccess = new GymMemberDBAccess();
    }

    @Override
    public List<Payment> getByMemberId(int memberId) throws ReadPaymentException {
        List<Payment> payments = new ArrayList<>();

        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SELECT_PAYMENTS_BY_MEMBER_ID_SQL)) {
                statement.setInt(1, memberId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        payments.add(mapPayment(resultSet));
                    }
                }
            }

            return payments;

        } catch (SQLException exception) {
            throw new ReadPaymentException(
                    String.valueOf(memberId),
                    "Erreur lors de la récupération des paiements du membre."
            );
        }
    }

    @Override
    public void deleteByMemberId(int memberId) throws DeletePaymentException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(DELETE_PAYMENTS_BY_MEMBER_ID_SQL)) {
                statement.setInt(1, memberId);
                statement.executeUpdate();
            }

        } catch (SQLException exception) {
            throw new DeletePaymentException(
                    String.valueOf(memberId),
                    "Erreur lors de la suppression des paiements du membre."
            );
        }
    }

    private Payment mapPayment(ResultSet resultSet) throws SQLException, ReadPaymentException {
        int paymentId = resultSet.getInt("id");

        try {
            int memberId = resultSet.getInt("billing");
            GymMember member = gymMemberDataAccess.getById(memberId);

            if (member == null) {
                throw new ReadPaymentException(
                        String.valueOf(memberId),
                        "Le membre lié au paiement est introuvable."
                );
            }

            return new Payment(
                    paymentId,
                    resultSet.getDouble("amount"),
                    resultSet.getDate("date_payment").toLocalDate(),
                    member
            );

        } catch (
                ReadGymMemberException |
                IllegalArgumentException exception
        ) {
            throw new ReadPaymentException(
                    String.valueOf(paymentId),
                    "Erreur lors de la création du paiement à partir des données récupérées."
            );
        }
    }

    private Connection getConnection() throws SQLException {
        return SingletonConnection.getInstance();
    }
}