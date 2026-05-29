package dataAccessPackage.searchDataAccess;

import dataAccessPackage.*;
import exceptionPackage.search.*;
import modelPackage.*;
import modelPackage.searchResult.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SearchDBAccess implements ISearchDA {

    private static final String SEARCH_APPOINTMENTS_BY_MEMBER_AND_DATE_RANGE_SQL = """
            SELECT a.id AS appointment_id,
                   coach_person.first_name AS coach_first_name,
                   coach_person.last_name AS coach_last_name,
                   ca.available_date,
                   ca.start_time,
                   ca.end_time,
                   a.objective,
                   a.status,
                   r.name AS room_name
            FROM appointment a
            INNER JOIN coach_availability ca ON a.availability_id = ca.id
            INNER JOIN coach c ON ca.person_id = c.person_id
            INNER JOIN person coach_person ON c.person_id = coach_person.id
            LEFT JOIN room r ON a.room_id = r.id
            WHERE a.member_id = ?
              AND ca.available_date BETWEEN ? AND ?
            ORDER BY ca.available_date, ca.start_time
            """;

    private static final String SEARCH_SPONSORED_MEMBERS_BY_SPONSOR_ID_SQL = """
            SELECT sponsored_person.id AS sponsored_member_id,
                   sponsored_person.first_name,
                   sponsored_person.last_name,
                   sponsored_person.email,
                   gm.is_active,
                   gm.weight,
                   gm.height,
                   s.type AS subscription_type,
                   s.price AS subscription_price,
                   s.duration_months AS subscription_duration_months
            FROM sponsorship sp
            INNER JOIN gym_member gm ON sp.sponsored_id = gm.person_id
            INNER JOIN person sponsored_person ON gm.person_id = sponsored_person.id
            INNER JOIN subscription s ON gm.enrollment = s.id
            WHERE sp.sponsor_id = ?
            ORDER BY sponsored_person.last_name, sponsored_person.first_name
            """;

    private static final String SEARCH_AVAILABLE_COACHES_BY_SPECIALITY_AND_DATE_RANGE_SQL = """
            SELECT ca.id AS availability_id,
                   coach_person.id AS coach_id,
                   coach_person.first_name AS coach_first_name,
                   coach_person.last_name AS coach_last_name,
                   c.has_degree,
                   ca.available_date,
                   ca.start_time,
                   ca.end_time
            FROM coach_availability ca
            INNER JOIN coach c ON ca.person_id = c.person_id
            INNER JOIN person coach_person ON c.person_id = coach_person.id
            INNER JOIN qualification q ON c.person_id = q.coach_id
            WHERE q.speciality_name = ?
              AND ca.available_date BETWEEN ? AND ?
              AND ca.is_booked = false
            ORDER BY ca.available_date, ca.start_time, coach_person.last_name, coach_person.first_name
            """;

    private Connection connection;

    public SearchDBAccess() {
    }

    @Override
    public List<AppointmentSearchResult> searchAppointmentsByMemberAndDateRange(
            int memberId,
            LocalDate startDate,
            LocalDate endDate
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

    @Override
    public List<SponsoredMemberSearchResult> searchSponsoredMembersBySponsorId(
            int sponsorId
    ) throws SearchException {
        List<SponsoredMemberSearchResult> results = new ArrayList<>();

        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SEARCH_SPONSORED_MEMBERS_BY_SPONSOR_ID_SQL)) {
                statement.setInt(1, sponsorId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        results.add(mapSponsoredMemberSearchResult(resultSet));
                    }
                }
            }

            return results;

        } catch (SQLException exception) {
            throw new SearchException(
                    "database",
                    "Erreur lors de la recherche des membres parrainés."
            );
        }
    }

    @Override
    public List<AvailableCoachSearchResult> searchAvailableCoachesBySpecialityAndDateRange(
            String specialityName,
            LocalDate startDate,
            LocalDate endDate
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

    private AppointmentSearchResult mapAppointmentSearchResult(ResultSet resultSet)
            throws SQLException, SearchException {
        int appointmentId = resultSet.getInt("appointment_id");

        try {
            return new AppointmentSearchResult(
                    appointmentId,
                    resultSet.getString("coach_first_name"),
                    resultSet.getString("coach_last_name"),
                    resultSet.getDate("available_date").toLocalDate(),
                    resultSet.getTime("start_time").toLocalTime(),
                    resultSet.getTime("end_time").toLocalTime(),
                    resultSet.getString("objective"),
                    getAppointmentStatus(resultSet.getInt("status")),
                    resultSet.getString("room_name")
            );

        } catch (IllegalArgumentException exception) {
            throw new SearchException(
                    String.valueOf(appointmentId),
                    "Erreur lors de la création du résultat de recherche des rendez-vous."
            );
        }
    }

    private SponsoredMemberSearchResult mapSponsoredMemberSearchResult(ResultSet resultSet)
            throws SQLException, SearchException {
        int sponsoredMemberId = resultSet.getInt("sponsored_member_id");

        try {
            return new SponsoredMemberSearchResult(
                    sponsoredMemberId,
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email"),
                    resultSet.getBoolean("is_active"),
                    resultSet.getDouble("weight"),
                    resultSet.getInt("height"),
                    SubscriptionType.fromDatabaseValue(resultSet.getString("subscription_type")),
                    resultSet.getDouble("subscription_price"),
                    resultSet.getInt("subscription_duration_months")
            );

        } catch (IllegalArgumentException exception) {
            throw new SearchException(
                    String.valueOf(sponsoredMemberId),
                    "Erreur lors de la création du résultat de recherche des membres parrainés."
            );
        }
    }

    private AvailableCoachSearchResult mapAvailableCoachSearchResult(ResultSet resultSet)
            throws SQLException {
        return new AvailableCoachSearchResult(
                resultSet.getInt("availability_id"),
                resultSet.getInt("coach_id"),
                resultSet.getString("coach_first_name"),
                resultSet.getString("coach_last_name"),
                resultSet.getBoolean("has_degree"),
                resultSet.getDate("available_date").toLocalDate(),
                resultSet.getTime("start_time").toLocalTime(),
                resultSet.getTime("end_time").toLocalTime()
        );
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
}