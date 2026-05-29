package dataAccessPackage.coachDataAccess;

import dataAccessPackage.SingletonConnection;
import exceptionPackage.coach.ReadCoachException;
import exceptionPackage.validation.*;
import modelPackage.Coach;
import modelPackage.Gender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoachDBAccess implements ICoachDA {

    private static final String SELECT_ALL_COACHES_SQL = """
            SELECT p.id,
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
            FROM person p
            INNER JOIN coach c ON p.id = c.person_id
            ORDER BY p.last_name, p.first_name
            """;

    private static final String SELECT_COACH_BY_ID_SQL = """
            SELECT p.id,
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
            FROM person p
            INNER JOIN coach c ON p.id = c.person_id
            WHERE p.id = ?
            """;

    private static final String SELECT_COACHES_BY_SPECIALITY_SQL = """
            SELECT p.id,
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
            FROM person p
            INNER JOIN coach c ON p.id = c.person_id
            INNER JOIN qualification q ON c.person_id = q.coach_id
            WHERE q.speciality_name = ?
            ORDER BY p.last_name, p.first_name
            """;

    private Connection connection;

    public CoachDBAccess() {
    }

    @Override
    public List<Coach> getAll() throws ReadCoachException {
        List<Coach> coaches = new ArrayList<>();

        try {
            connection = getConnection();

            try (
                    PreparedStatement statement = connection.prepareStatement(SELECT_ALL_COACHES_SQL);
                    ResultSet resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    coaches.add(mapCoach(resultSet));
                }
            }

            return coaches;

        } catch (SQLException exception) {
            throw new ReadCoachException(
                    "database",
                    "Erreur lors de la récupération des coachs."
            );
        }
    }

    @Override
    public Coach getById(int id) throws ReadCoachException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SELECT_COACH_BY_ID_SQL)) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapCoach(resultSet);
                    }

                    return null;
                }
            }

        } catch (SQLException exception) {
            throw new ReadCoachException(
                    String.valueOf(id),
                    "Erreur lors de la récupération du coach."
            );
        }
    }

    @Override
    public List<Coach> getBySpecialityName(String specialityName) throws ReadCoachException {
        List<Coach> coaches = new ArrayList<>();

        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SELECT_COACHES_BY_SPECIALITY_SQL)) {
                statement.setString(1, specialityName);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        coaches.add(mapCoach(resultSet));
                    }
                }
            }

            return coaches;

        } catch (SQLException exception) {
            throw new ReadCoachException(
                    specialityName,
                    "Erreur lors de la récupération des coachs par spécialité."
            );
        }
    }

    private Coach mapCoach(ResultSet resultSet) throws SQLException, ReadCoachException {
        int id = resultSet.getInt("id");

        try {
            return new Coach(
                    id,
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

        } catch (
                InvalidFirstNameException |
                InvalidLastNameException |
                InvalidGenderException |
                InvalidEmailException |
                InvalidPhoneException |
                InvalidLockerNumberException |
                InvalidUsernameException |
                InvalidPasswordException |
                IllegalArgumentException exception
        ) {
            throw new ReadCoachException(
                    String.valueOf(id),
                    "Erreur lors de la création du coach à partir des données récupérées."
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