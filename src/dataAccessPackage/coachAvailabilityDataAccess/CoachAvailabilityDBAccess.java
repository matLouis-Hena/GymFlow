package dataAccessPackage.coachAvailabilityDataAccess;

import dataAccessPackage.SingletonConnection;
import exceptionPackage.coachAvailability.*;
import exceptionPackage.validation.*;
import modelPackage.Coach;
import modelPackage.CoachAvailability;
import modelPackage.Gender;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoachAvailabilityDBAccess implements ICoachAvailabilityDA {

    private static final String SELECT_ALL_AVAILABILITIES_SQL = """
            SELECT ca.id,
                   ca.available_date,
                   ca.start_time,
                   ca.end_time,
                   ca.is_booked,
                   p.id AS coach_id,
                   p.first_name,
                   p.last_name,
                   p.birth_date,
                   p.gender,
                   p.email,
                   p.phone,
                   p.locker_number,
                   p.username,
                   p.password,
                   c.has_degree
            FROM coach_availability ca
            INNER JOIN coach c ON ca.person_id = c.person_id
            INNER JOIN person p ON c.person_id = p.id
            ORDER BY ca.available_date, ca.start_time
            """;

    private static final String SELECT_AVAILABILITY_BY_ID_SQL = """
            SELECT ca.id,
                   ca.available_date,
                   ca.start_time,
                   ca.end_time,
                   ca.is_booked,
                   p.id AS coach_id,
                   p.first_name,
                   p.last_name,
                   p.birth_date,
                   p.gender,
                   p.email,
                   p.phone,
                   p.locker_number,
                   p.username,
                   p.password,
                   c.has_degree
            FROM coach_availability ca
            INNER JOIN coach c ON ca.person_id = c.person_id
            INNER JOIN person p ON c.person_id = p.id
            WHERE ca.id = ?
            """;

    private static final String SELECT_AVAILABLE_BY_SPECIALITY_AND_DATE_RANGE_SQL = """
            SELECT ca.id,
                   ca.available_date,
                   ca.start_time,
                   ca.end_time,
                   ca.is_booked,
                   p.id AS coach_id,
                   p.first_name,
                   p.last_name,
                   p.birth_date,
                   p.gender,
                   p.email,
                   p.phone,
                   p.locker_number,
                   p.username,
                   p.password,
                   c.has_degree
            FROM coach_availability ca
            INNER JOIN coach c ON ca.person_id = c.person_id
            INNER JOIN person p ON c.person_id = p.id
            INNER JOIN qualification q ON c.person_id = q.coach_id
            WHERE q.speciality_name = ?
              AND ca.available_date BETWEEN ? AND ?
              AND ca.is_booked = false
            ORDER BY ca.available_date, ca.start_time
            """;

    private static final String UPDATE_BOOKING_STATUS_SQL = """
            UPDATE coach_availability
            SET is_booked = ?
            WHERE id = ?
            """;

    private Connection connection;

    public CoachAvailabilityDBAccess() {
    }

    @Override
    public List<CoachAvailability> getAll() throws ReadCoachAvailabilityException {
        List<CoachAvailability> availabilities = new ArrayList<>();

        try {
            connection = getConnection();

            try (
                    PreparedStatement statement = connection.prepareStatement(SELECT_ALL_AVAILABILITIES_SQL);
                    ResultSet resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    availabilities.add(mapCoachAvailability(resultSet));
                }
            }

            return availabilities;

        } catch (SQLException exception) {
            throw new ReadCoachAvailabilityException(
                    "database",
                    "Erreur lors de la récupération des disponibilités des coachs."
            );
        }
    }

    @Override
    public CoachAvailability getById(int id) throws ReadCoachAvailabilityException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SELECT_AVAILABILITY_BY_ID_SQL)) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapCoachAvailability(resultSet);
                    }

                    return null;
                }
            }

        } catch (SQLException exception) {
            throw new ReadCoachAvailabilityException(
                    String.valueOf(id),
                    "Erreur lors de la récupération de la disponibilité du coach."
            );
        }
    }

    @Override
    public List<CoachAvailability> getAvailableBySpecialityAndDateRange(
            String specialityName,
            java.time.LocalDate startDate,
            java.time.LocalDate endDate
    ) throws ReadCoachAvailabilityException {
        List<CoachAvailability> availabilities = new ArrayList<>();

        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SELECT_AVAILABLE_BY_SPECIALITY_AND_DATE_RANGE_SQL)) {
                statement.setString(1, specialityName);
                statement.setDate(2, Date.valueOf(startDate));
                statement.setDate(3, Date.valueOf(endDate));

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        availabilities.add(mapCoachAvailability(resultSet));
                    }
                }
            }

            return availabilities;

        } catch (SQLException exception) {
            throw new ReadCoachAvailabilityException(
                    specialityName,
                    "Erreur lors de la récupération des disponibilités par spécialité."
            );
        }
    }

    @Override
    public void markAsBooked(int id) throws UpdateCoachAvailabilityException {
        updateBookingStatus(id, true);
    }

    @Override
    public void markAsAvailable(int id) throws UpdateCoachAvailabilityException {
        updateBookingStatus(id, false);
    }

    private void updateBookingStatus(int id, boolean isBooked) throws UpdateCoachAvailabilityException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(UPDATE_BOOKING_STATUS_SQL)) {
                statement.setBoolean(1, isBooked);
                statement.setInt(2, id);

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new UpdateCoachAvailabilityException(
                            String.valueOf(id),
                            "Aucune disponibilité n'a été modifiée."
                    );
                }
            }

        } catch (SQLException exception) {
            throw new UpdateCoachAvailabilityException(
                    String.valueOf(id),
                    "Erreur lors de la modification de la disponibilité du coach."
            );
        }
    }

    private CoachAvailability mapCoachAvailability(ResultSet resultSet)
            throws SQLException, ReadCoachAvailabilityException {
        int availabilityId = resultSet.getInt("id");

        try {
            Coach coach = new Coach(
                    resultSet.getInt("coach_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getDate("birth_date").toLocalDate(),
                    Gender.fromDatabaseValue(resultSet.getString("gender")),
                    resultSet.getString("email"),
                    resultSet.getString("phone"),
                    getNullableInteger(resultSet, "locker_number"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getBoolean("has_degree")
            );

            return new CoachAvailability(
                    availabilityId,
                    resultSet.getDate("available_date").toLocalDate(),
                    resultSet.getTime("start_time"),
                    resultSet.getTime("end_time"),
                    resultSet.getBoolean("is_booked"),
                    coach
            );

        } catch (
                InvalidFirstNameException |
                InvalidLastNameException |
                InvalidGenderException |
                InvalidEmailException |
                InvalidPhoneException |
                InvalidLockerNumberException |
                InvalidUsernameException |
                InvalidPasswordException |
                InvalidDateException |
                InvalidTimeException |
                IllegalArgumentException exception
        ) {
            throw new ReadCoachAvailabilityException(
                    String.valueOf(availabilityId),
                    "Erreur lors de la création de la disponibilité à partir des données récupérées."
            );
        }
    }

    private Integer getNullableInteger(ResultSet resultSet, String columnName) throws SQLException {
        int value = resultSet.getInt(columnName);

        if (resultSet.wasNull()) {
            return null;
        }

        return value;
    }

    private Connection getConnection() throws SQLException {
        return SingletonConnection.getInstance();
    }
}