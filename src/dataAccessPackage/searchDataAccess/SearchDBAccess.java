package dataAccessPackage.searchDataAccess;

import dataAccessPackage.SingletonConnection;
import exceptionPackage.search.SearchException;
import modelPackage.AppointmentStatus;
import modelPackage.SubscriptionType;
import modelPackage.searchResult.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchDBAccess implements ISearchDA {

    private static final String SEARCH_APPOINTMENTS_BY_MEMBER_AND_DATE_RANGE_SQL = """
            SELECT a.id AS appointment_id,
                   member_person.id AS member_id,
                   CONCAT(member_person.first_name, ' ', member_person.last_name) AS member_full_name,
                   CONCAT(coach_person.first_name, ' ', coach_person.last_name) AS coach_full_name,
                   ca.available_date,
                   ca.start_time,
                   ca.end_time,
                   r.name AS room_name,
                   a.objective,
                   a.status
            FROM appointment a
            INNER JOIN gym_member gm ON a.member_id = gm.person_id
            INNER JOIN person member_person ON gm.person_id = member_person.id
            INNER JOIN coach_availability ca ON a.availability_id = ca.id
            INNER JOIN coach c ON ca.person_id = c.person_id
            INNER JOIN person coach_person ON c.person_id = coach_person.id
            LEFT JOIN room r ON a.room_id = r.id
            WHERE member_person.id = ?
              AND ca.available_date BETWEEN ? AND ?
            ORDER BY ca.available_date, ca.start_time
            """;

    private Connection connection;

    public SearchDBAccess() {
    }

    @Override
    public List<AppointmentSearchResult> searchAppointmentsByMemberAndDateRange(
            int memberId,
            java.time.LocalDate startDate,
            java.time.LocalDate endDate
    ) throws SearchException {
        List<AppointmentSearchResult> results = new ArrayList<>();

        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SEARCH_APPOINTMENTS_BY_MEMBER_AND_DATE_RANGE_SQL)) {
                statement.setInt(1, memberId);
                statement.setDate(2, Date.valueOf(startDate));
                statement.setDate(3, Date.valueOf(endDate));

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        results.add(mapAppointmentSearchResult(resultSet));
                    }
                }
            }

            return results;

        } catch (SQLException exception) {
            throw new SearchException(
                    "database",
                    "Erreur lors de la recherche des rendez-vous."
            );
        }
    }

    private AppointmentSearchResult mapAppointmentSearchResult(ResultSet resultSet)
            throws SQLException, SearchException {
        int appointmentId = resultSet.getInt("appointment_id");

        try {
            return new AppointmentSearchResult(
                    appointmentId,
                    resultSet.getInt("member_id"),
                    resultSet.getString("member_full_name"),
                    resultSet.getString("coach_full_name"),
                    resultSet.getDate("available_date").toLocalDate(),
                    resultSet.getTime("start_time").toLocalTime(),
                    resultSet.getTime("end_time").toLocalTime(),
                    resultSet.getString("room_name"),
                    resultSet.getString("objective"),
                    getAppointmentStatus(resultSet.getInt("status"))
            );

        } catch (IllegalArgumentException exception) {
            throw new SearchException(
                    String.valueOf(appointmentId),
                    "Erreur lors de la création du résultat de recherche."
            );
        }
    }

    private AppointmentStatus getAppointmentStatus(int statusIndex) {
        AppointmentStatus[] statuses = AppointmentStatus.values();

        if (statusIndex < 0 || statusIndex >= statuses.length) {
            throw new IllegalArgumentException("Statut de rendez-vous invalide.");
        }

        return statuses[statusIndex];
    }

    private Connection getConnection() throws SQLException {
        return SingletonConnection.getInstance();
    }

    @Override
    public List<AvailableCoachSearchResult> searchAvailableCoachesBySpecialityAndDateRange(
            String specialityName,
            java.time.LocalDate startDate,
            java.time.LocalDate endDate
    ) throws SearchException {
        List<AvailableCoachSearchResult> results = new ArrayList<>();

        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SEARCH_AVAILABLE_COACHES_BY_SPECIALITY_AND_DATE_RANGE_SQL)) {
                statement.setString(1, specialityName);
                statement.setDate(2, Date.valueOf(startDate));
                statement.setDate(3, Date.valueOf(endDate));

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        results.add(mapAvailableCoachSearchResult(resultSet));
                    }
                }
            }

            return results;

        } catch (SQLException exception) {
            throw new SearchException(
                    "database",
                    "Erreur lors de la recherche des coachs disponibles."
            );
        }
    }

    private AvailableCoachSearchResult mapAvailableCoachSearchResult(ResultSet resultSet)
            throws SQLException {
        return new AvailableCoachSearchResult(
                resultSet.getInt("availability_id"),
                resultSet.getInt("coach_id"),
                resultSet.getString("coach_full_name"),
                resultSet.getString("speciality_name"),
                resultSet.getDate("available_date").toLocalDate(),
                resultSet.getTime("start_time").toLocalTime(),
                resultSet.getTime("end_time").toLocalTime(),
                resultSet.getBoolean("has_degree")
        );
    }

    private static final String SEARCH_AVAILABLE_COACHES_BY_SPECIALITY_AND_DATE_RANGE_SQL = """
        SELECT ca.id AS availability_id,
               coach_person.id AS coach_id,
               CONCAT(coach_person.first_name, ' ', coach_person.last_name) AS coach_full_name,
               s.name AS speciality_name,
               ca.available_date,
               ca.start_time,
               ca.end_time,
               c.has_degree
        FROM coach_availability ca
        INNER JOIN coach c ON ca.person_id = c.person_id
        INNER JOIN person coach_person ON c.person_id = coach_person.id
        INNER JOIN qualification q ON c.person_id = q.coach_id
        INNER JOIN speciality s ON q.speciality_name = s.name
        WHERE s.name = ?
          AND ca.available_date BETWEEN ? AND ?
          AND ca.is_booked = false
        ORDER BY ca.available_date, ca.start_time, coach_person.last_name, coach_person.first_name
        """;

    @Override
    public List<PaymentSearchResult> searchPaymentsByMemberAndDateRange(
            int memberId,
            java.time.LocalDate startDate,
            java.time.LocalDate endDate
    ) throws SearchException {
        List<PaymentSearchResult> results = new ArrayList<>();

        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SEARCH_PAYMENTS_BY_MEMBER_AND_DATE_RANGE_SQL)) {
                statement.setInt(1, memberId);
                statement.setDate(2, Date.valueOf(startDate));
                statement.setDate(3, Date.valueOf(endDate));

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        results.add(mapPaymentSearchResult(resultSet));
                    }
                }
            }

            return results;

        } catch (SQLException exception) {
            throw new SearchException(
                    "database",
                    "Erreur lors de la recherche des paiements."
            );
        }
    }

    private PaymentSearchResult mapPaymentSearchResult(ResultSet resultSet)
            throws SQLException, SearchException {
        int paymentId = resultSet.getInt("payment_id");

        try {
            return new PaymentSearchResult(
                    paymentId,
                    resultSet.getInt("member_id"),
                    resultSet.getString("member_full_name"),
                    resultSet.getDouble("amount"),
                    resultSet.getDate("date_payment").toLocalDate(),
                    SubscriptionType.fromDatabaseValue(resultSet.getString("subscription_type")),
                    resultSet.getInt("subscription_duration_months")
            );

        } catch (IllegalArgumentException exception) {
            throw new SearchException(
                    String.valueOf(paymentId),
                    "Erreur lors de la création du résultat de paiement."
            );
        }
    }

    private static final String SEARCH_PAYMENTS_BY_MEMBER_AND_DATE_RANGE_SQL = """
        SELECT pay.id AS payment_id,
               member_person.id AS member_id,
               CONCAT(member_person.first_name, ' ', member_person.last_name) AS member_full_name,
               pay.amount,
               pay.date_payment,
               s.type AS subscription_type,
               s.duration_months AS subscription_duration_months
        FROM payment pay
        INNER JOIN gym_member gm ON pay.billing = gm.person_id
        INNER JOIN person member_person ON gm.person_id = member_person.id
        INNER JOIN subscription s ON gm.enrollment = s.id
        WHERE member_person.id = ?
          AND pay.date_payment BETWEEN ? AND ?
        ORDER BY pay.date_payment DESC, pay.id DESC
        """;
}