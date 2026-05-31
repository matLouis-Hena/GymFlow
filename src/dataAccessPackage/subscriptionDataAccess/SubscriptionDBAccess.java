package dataAccessPackage.subscriptionDataAccess;

import dataAccessPackage.SingletonConnection;
import exceptionPackage.subscription.ReadSubscriptionException;
import exceptionPackage.validation.InvalidDurationException;
import exceptionPackage.validation.InvalidPriceException;
import modelPackage.Subscription;
import modelPackage.SubscriptionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionDBAccess implements ISubscriptionDA {

    private static final String SELECT_ALL_SUBSCRIPTIONS_SQL = """
            SELECT id, type, price, duration_months
            FROM subscription
            ORDER BY type, duration_months
            """;

    private static final String SELECT_SUBSCRIPTION_BY_ID_SQL = """
            SELECT id, type, price, duration_months
            FROM subscription
            WHERE id = ?
            """;

    private Connection connection;

    public SubscriptionDBAccess() {
    }

    @Override
    public List<Subscription> getAll() throws ReadSubscriptionException {
        List<Subscription> subscriptions = new ArrayList<>();

        try {
            connection = getConnection();

            try (
                    PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SUBSCRIPTIONS_SQL);
                    ResultSet resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    subscriptions.add(mapSubscription(resultSet));
                }
            }

            return subscriptions;

        } catch (SQLException exception) {
            throw new ReadSubscriptionException(
                    "database",
                    "Erreur lors de la récupération des abonnements."
            );
        }
    }

    @Override
    public Subscription getById(int id) throws ReadSubscriptionException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SELECT_SUBSCRIPTION_BY_ID_SQL)) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapSubscription(resultSet);
                    }

                    return null;
                }
            }

        } catch (SQLException exception) {
            throw new ReadSubscriptionException(
                    String.valueOf(id),
                    "Erreur lors de la récupération de l'abonnement."
            );
        }
    }

    private Subscription mapSubscription(ResultSet resultSet)
            throws SQLException, ReadSubscriptionException {
        int id = resultSet.getInt("id");

        try {
            return new Subscription(
                    id,
                    SubscriptionType.fromDatabaseValue(resultSet.getString("type")),
                    resultSet.getDouble("price"),
                    resultSet.getInt("duration_months")
            );

        } catch (
                InvalidPriceException |
                InvalidDurationException |
                IllegalArgumentException exception
        ) {
            throw new ReadSubscriptionException(
                    String.valueOf(id),
                    "Erreur lors de la création de l'abonnement à partir des données récupérées."
            );
        }
    }

    private Connection getConnection() throws SQLException {
        return SingletonConnection.getInstance();
    }
}
