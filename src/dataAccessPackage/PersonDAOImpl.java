package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.Gender;
import modelPackage.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAOImpl implements PersonDAO {

    private static final String INSERT_PERSON_SQL = """
        INSERT INTO person
        (first_name, last_name, birth_date, gender, email, phone, locker_number, username, password)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

    private static final String SELECT_PERSON_BY_ID_SQL =
            "SELECT * FROM person WHERE id = ?";

    private static final String SELECT_ALL_PERSONS_SQL =
            "SELECT * FROM person";

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

    private static final String DELETE_PERSON_SQL =
            "DELETE FROM person WHERE id = ?";

    private final Connection connection;

    public PersonDAOImpl() throws SQLException {
        connection = SingletonConnection.getInstance();
    }

    @Override
    public int insert(Person person) throws DataAccessException {
        try (PreparedStatement stmt = connection.prepareStatement(
                INSERT_PERSON_SQL,
                Statement.RETURN_GENERATED_KEYS
        )) {
            fillPersonInsertOrUpdateStatement(stmt, person);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

            throw new SQLException("No generated ID returned");

        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de l'insertion de la personne", e);
        }
    }

    @Override
    public Person getById(int id) throws DataAccessException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_PERSON_BY_ID_SQL)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapPerson(rs);
                }
            }

            return null;

        } catch (Exception e) {
            throw new DataAccessException("Erreur lors de la récupération de la personne", e);
        }
    }

    @Override
    public List<Person> getAll() throws DataAccessException {
        List<Person> persons = new ArrayList<>();

        try (
                PreparedStatement stmt = connection.prepareStatement(SELECT_ALL_PERSONS_SQL);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                persons.add(mapPerson(rs));
            }

            return persons;

        } catch (Exception e) {
            throw new DataAccessException("Erreur lors de la récupération des personnes", e);
        }
    }

    @Override
    public void update(Person person) throws DataAccessException {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_PERSON_SQL)) {
            fillPersonInsertOrUpdateStatement(stmt, person);
            stmt.setInt(10, person.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la mise à jour de la personne", e);
        }
    }

    @Override
    public void delete(int id) throws DataAccessException {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_PERSON_SQL)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la suppression de la personne", e);
        }
    }

    private void fillPersonInsertOrUpdateStatement(PreparedStatement stmt, Person person) throws SQLException {
        stmt.setString(1, person.getFirstName());
        stmt.setString(2, person.getLastName());
        stmt.setDate(3, Date.valueOf(person.getBirthDate()));
        stmt.setString(4, person.getGender().getDatabaseValue());
        stmt.setString(5, person.getEmail());

        if (person.getPhone() != null) {
            stmt.setString(6, person.getPhone());
        } else {
            stmt.setNull(6, Types.VARCHAR);
        }

        if (person.getLockerNumber() != null) {
            stmt.setInt(7, person.getLockerNumber());
        } else {
            stmt.setNull(7, Types.INTEGER);
        }

        stmt.setString(8, person.getUsername());
        stmt.setString(9, person.getPassword());
    }

    private Person mapPerson(ResultSet rs) throws Exception {
        return new Person(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getDate("birth_date").toLocalDate(),
                Gender.fromDatabaseValue(rs.getString("gender")),
                rs.getString("email"),
                rs.getString("phone"),
                (Integer) rs.getObject("locker_number"),
                rs.getString("username"),
                rs.getString("password")
        );
    }
}