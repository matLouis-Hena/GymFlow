package dataAccessPackage.specialityDataAccess;

import dataAccessPackage.SingletonConnection;
import exceptionPackage.speciality.ReadSpecialityException;
import exceptionPackage.validation.InvalidDescriptionException;
import exceptionPackage.validation.InvalidNameException;
import modelPackage.Speciality;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SpecialityDBAccess implements ISpecialityDA {

    private static final String SELECT_ALL_SPECIALITIES_SQL = """
            SELECT name, description
            FROM speciality
            ORDER BY name
            """;

    private static final String SELECT_SPECIALITY_BY_NAME_SQL = """
            SELECT name, description
            FROM speciality
            WHERE name = ?
            """;

    private Connection connection;

    public SpecialityDBAccess() {
    }

    @Override
    public List<Speciality> getAll() throws ReadSpecialityException {
        List<Speciality> specialities = new ArrayList<>();

        try {
            connection = getConnection();

            try (
                    PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SPECIALITIES_SQL);
                    ResultSet resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    specialities.add(mapSpeciality(resultSet));
                }
            }

            return specialities;

        } catch (SQLException exception) {
            throw new ReadSpecialityException(
                    "database",
                    "Erreur lors de la récupération des spécialités."
            );
        }
    }

    @Override
    public Speciality getByName(String name) throws ReadSpecialityException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SELECT_SPECIALITY_BY_NAME_SQL)) {
                statement.setString(1, name);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapSpeciality(resultSet);
                    }

                    return null;
                }
            }

        } catch (SQLException exception) {
            throw new ReadSpecialityException(
                    name,
                    "Erreur lors de la récupération de la spécialité."
            );
        }
    }

    private Speciality mapSpeciality(ResultSet resultSet)
            throws SQLException, ReadSpecialityException {
        String name = resultSet.getString("name");

        try {
            return new Speciality(
                    name,
                    resultSet.getString("description")
            );

        } catch (InvalidNameException | InvalidDescriptionException exception) {
            throw new ReadSpecialityException(
                    name,
                    "Erreur lors de la création de la spécialité à partir des données récupérées."
            );
        }
    }

    private Connection getConnection() throws SQLException {
        return SingletonConnection.getInstance();
    }
}