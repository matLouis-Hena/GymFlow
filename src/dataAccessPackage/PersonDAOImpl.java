package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAOImpl implements PersonDAO {

    private Connection connection;

    public PersonDAOImpl() throws SQLException {
        connection = DatabaseConnection.getInstance();
    }

    @Override
    public int insert(Person person) throws DataAccessException {

        String sql = """
            INSERT INTO person
            (first_name, last_name, birth_date, gender,
             email, phone, locker_number, username, password)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (
                PreparedStatement stmt =
                        connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {

            stmt.setString(1, person.getFirstName());
            stmt.setString(2, person.getLastName());
            stmt.setDate(3, Date.valueOf(person.getBirthDate()));
            stmt.setString(4, String.valueOf(person.getGender()));
            stmt.setString(5, person.getEmail());

            if(person.getPhone() != null) {
                stmt.setString(6, person.getPhone());
            } else {
                stmt.setNull(6, Types.VARCHAR);
            }

            if(person.getLockerNumber() != null) {
                stmt.setInt(7, person.getLockerNumber());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }

            stmt.setString(8, person.getUsername());
            stmt.setString(9, person.getPassword());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();

            if(generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }

            throw new SQLException("No ID generated");

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Erreur lors de l'insertion de la personne",
                    e
            );
        }
    }

    @Override
    public Person getById(int id) throws DataAccessException {

        String sql = "SELECT * FROM person WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                return new Person(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("birth_date").toLocalDate(),
                        rs.getString("gender").charAt(0),
                        rs.getString("email"),
                        rs.getString("phone"),
                        (Integer) rs.getObject("locker_number"),
                        rs.getString("username"),
                        rs.getString("password")
                );
            }

            return null;

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Erreur lors de la récupération de la personne",
                    e
            );

        } catch (Exception e) {

            throw new DataAccessException(
                    "Erreur lors du mapping de la personne",
                    e
            );
        }
    }

    @Override
    public List<Person> getAll() throws DataAccessException {

        List<Person> persons = new ArrayList<>();

        String sql = "SELECT * FROM person";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                Person person = new Person(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("birth_date").toLocalDate(),
                        rs.getString("gender").charAt(0),
                        rs.getString("email"),
                        rs.getString("phone"),
                        (Integer) rs.getObject("locker_number"),
                        rs.getString("username"),
                        rs.getString("password")
                );

                persons.add(person);
            }

            return persons;

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Erreur lors de la récupération des personnes",
                    e
            );

        } catch (Exception e) {

            throw new DataAccessException(
                    "Erreur lors du mapping des personnes",
                    e
            );
        }
    }

    @Override
    public void update(Person person) throws DataAccessException {

        String sql = """
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

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, person.getFirstName());
            stmt.setString(2, person.getLastName());
            stmt.setDate(3, Date.valueOf(person.getBirthDate()));
            stmt.setString(4, String.valueOf(person.getGender()));
            stmt.setString(5, person.getEmail());

            if(person.getPhone() != null) {
                stmt.setString(6, person.getPhone());
            } else {
                stmt.setNull(6, Types.VARCHAR);
            }

            if(person.getLockerNumber() != null) {
                stmt.setInt(7, person.getLockerNumber());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }

            stmt.setString(8, person.getUsername());
            stmt.setString(9, person.getPassword());

            stmt.setInt(10, person.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Erreur lors de la mise à jour de la personne",
                    e
            );
        }
    }

    @Override
    public void delete(int id) throws DataAccessException {

        String sql = "DELETE FROM person WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Erreur lors de la suppression de la personne",
                    e
            );
        }
    }
}