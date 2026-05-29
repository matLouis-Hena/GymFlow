package dataAccessPackage.personDataAccess;

import dataAccessPackage.SingletonConnection;
import exceptionPackage.person.AddPersonException;
import exceptionPackage.person.DeletePersonException;
import exceptionPackage.person.ReadPersonException;
import exceptionPackage.person.UpdatePersonException;
import exceptionPackage.validation.*;
import modelPackage.Gender;
import modelPackage.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDBAccess implements IPersonDA {

    private static final String INSERT_PERSON_SQL = """
            INSERT INTO person
            (first_name, last_name, birth_date, gender, email, phone, locker_number, username, password)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String SELECT_PERSON_BY_ID_SQL = """
            SELECT id, first_name, last_name, birth_date, gender, email, phone, locker_number, username, password
            FROM person
            WHERE id = ?
            """;

    private static final String SELECT_ALL_PERSONS_SQL = """
            SELECT id, first_name, last_name, birth_date, gender, email, phone, locker_number, username, password
            FROM person
            ORDER BY last_name, first_name
            """;

    private static final String UPDATE_PERSON_SQL = """
            UPDATE person
            SET first_name = ?,
                last_name = ?,
                birth_date = ?,
                gender = ?,
                email = ?,
                phone = ?,
                locker_number = ?,
                username = ?,
                password = ?
            WHERE id = ?
            """;

    private static final String DELETE_PERSON_SQL = """
            DELETE FROM person
            WHERE id = ?
            """;

    private Connection connection;

    public PersonDBAccess() {
    }

    @Override
    public int insert(Person person) throws AddPersonException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(
                    INSERT_PERSON_SQL,
                    Statement.RETURN_GENERATED_KEYS
            )) {
                fillPersonInsertOrUpdateStatement(statement, person);

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new AddPersonException("person", "Aucune personne n'a été ajoutée.");
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }

                    throw new AddPersonException(
                            "person_id",
                            "Impossible de récupérer l'identifiant de la personne créée."
                    );
                }
            }

        } catch (SQLException exception) {
            throw new AddPersonException("database", "Erreur lors de l'insertion de la personne.");
        }
    }

    @Override
    public Person getById(int id) throws ReadPersonException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SELECT_PERSON_BY_ID_SQL)) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapPerson(resultSet);
                    }

                    return null;
                }
            }

        } catch (SQLException exception) {
            throw new ReadPersonException(String.valueOf(id), "Erreur lors de la récupération de la personne.");
        }
    }

    @Override
    public List<Person> getAll() throws ReadPersonException {
        List<Person> persons = new ArrayList<>();

        try {
            connection = getConnection();

            try (
                    PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PERSONS_SQL);
                    ResultSet resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    persons.add(mapPerson(resultSet));
                }
            }

            return persons;

        } catch (SQLException exception) {
            throw new ReadPersonException("database", "Erreur lors de la récupération des personnes.");
        }
    }

    @Override
    public void update(Person person) throws UpdatePersonException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(UPDATE_PERSON_SQL)) {
                fillPersonInsertOrUpdateStatement(statement, person);
                statement.setInt(10, person.getId());

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new UpdatePersonException(
                            String.valueOf(person.getId()),
                            "Aucune personne n'a été modifiée."
                    );
                }
            }

        } catch (SQLException exception) {
            throw new UpdatePersonException("database", "Erreur lors de la mise à jour de la personne.");
        }
    }

    @Override
    public void delete(int id) throws DeletePersonException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(DELETE_PERSON_SQL)) {
                statement.setInt(1, id);

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DeletePersonException(
                            String.valueOf(id),
                            "Aucune personne n'a été supprimée."
                    );
                }
            }

        } catch (SQLException exception) {
            throw new DeletePersonException("database", "Erreur lors de la suppression de la personne.");
        }
    }

    private void fillPersonInsertOrUpdateStatement(PreparedStatement statement, Person person) throws SQLException {
        statement.setString(1, person.getFirstName());
        statement.setString(2, person.getLastName());
        statement.setDate(3, Date.valueOf(person.getBirthDate()));
        statement.setString(4, person.getGender().getDatabaseValue());
        statement.setString(5, person.getEmail());

        if (person.getPhone() == null || person.getPhone().isBlank()) {
            statement.setNull(6, Types.VARCHAR);
        } else {
            statement.setString(6, person.getPhone());
        }

        if (person.getLockerNumber() == null) {
            statement.setNull(7, Types.INTEGER);
        } else {
            statement.setInt(7, person.getLockerNumber());
        }

        statement.setString(8, person.getUsername());
        statement.setString(9, person.getPassword());
    }

    private Person mapPerson(ResultSet resultSet) throws SQLException, ReadPersonException {
        int id = resultSet.getInt("id");

        try {
            return new Person(
                    id,
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getDate("birth_date").toLocalDate(),
                    Gender.fromDatabaseValue(resultSet.getString("gender")),
                    resultSet.getString("email"),
                    resultSet.getString("phone"),
                    getNullableInteger(resultSet, "locker_number"),
                    resultSet.getString("username"),
                    resultSet.getString("password")
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
                IllegalArgumentException |
                NullPointerException exception
        ) {
            throw new ReadPersonException(
                    String.valueOf(id),
                    "Erreur lors de la création de la personne à partir des données récupérées."
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