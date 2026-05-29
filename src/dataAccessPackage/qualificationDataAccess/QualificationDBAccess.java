package dataAccessPackage.qualificationDataAccess;

import dataAccessPackage.SingletonConnection;
import exceptionPackage.qualification.ReadQualificationException;
import exceptionPackage.validation.InvalidDescriptionException;
import exceptionPackage.validation.InvalidNameException;
import modelPackage.Speciality;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QualificationDBAccess implements IQualificationDA {

    private static final String SELECT_SPECIALITIES_BY_COACH_ID_SQL = """
            SELECT s.name, s.description
            FROM qualification q
            INNER JOIN speciality s ON q.speciality_name = s.name
            WHERE q.coach_id = ?
            ORDER BY s.name
            """;

    private static final String IS_COACH_QUALIFIED_FOR_SPECIALITY_SQL = """
            SELECT COUNT(*) AS qualification_count
            FROM qualification
            WHERE coach_id = ?
              AND speciality_name = ?
            """;

    private Connection connection;

    public QualificationDBAccess() {
    }

    @Override
    public List<Speciality> getSpecialitiesByCoachId(int coachId)
            throws ReadQualificationException {
        List<Speciality> specialities = new ArrayList<>();

        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SELECT_SPECIALITIES_BY_COACH_ID_SQL)) {
                statement.setInt(1, coachId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        specialities.add(mapSpeciality(resultSet));
                    }
                }
            }

            return specialities;

        } catch (SQLException exception) {
            throw new ReadQualificationException(
                    String.valueOf(coachId),
                    "Erreur lors de la récupération des spécialités du coach."
            );
        }
    }

    @Override
    public boolean isCoachQualifiedForSpeciality(int coachId, String specialityName)
            throws ReadQualificationException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(IS_COACH_QUALIFIED_FOR_SPECIALITY_SQL)) {
                statement.setInt(1, coachId);
                statement.setString(2, specialityName);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("qualification_count") > 0;
                    }

                    return false;
                }
            }

        } catch (SQLException exception) {
            throw new ReadQualificationException(
                    String.valueOf(coachId),
                    "Erreur lors de la vérification de la qualification du coach."
            );
        }
    }

    private Speciality mapSpeciality(ResultSet resultSet)
            throws SQLException, ReadQualificationException {
        String name = resultSet.getString("name");

        try {
            return new Speciality(
                    name,
                    resultSet.getString("description")
            );

        } catch (InvalidNameException | InvalidDescriptionException exception) {
            throw new ReadQualificationException(
                    name,
                    "Erreur lors de la création de la spécialité à partir des données récupérées."
            );
        }
    }

    private Connection getConnection() throws SQLException {
        return SingletonConnection.getInstance();
    }
}