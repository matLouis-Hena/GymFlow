package dataAccessPackage;

import exceptionPackage.gymMember.AddGymMemberException;
import exceptionPackage.gymMember.DeleteGymMemberException;
import exceptionPackage.InvalidDurationException;
import exceptionPackage.InvalidEmailException;
import exceptionPackage.InvalidFirstNameException;
import exceptionPackage.InvalidGenderException;
import exceptionPackage.InvalidHeightException;
import exceptionPackage.InvalidLastNameException;
import exceptionPackage.InvalidLockerNumberException;
import exceptionPackage.InvalidPasswordException;
import exceptionPackage.InvalidPhoneException;
import exceptionPackage.InvalidPriceException;
import exceptionPackage.InvalidUsernameException;
import exceptionPackage.InvalidWeightException;
import exceptionPackage.gymMember.ReadGymMemberException;
import exceptionPackage.gymMember.UpdateGymMemberException;
import modelPackage.Gender;
import modelPackage.GymMember;
import modelPackage.Subscription;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class GymMemberDAOImpl implements GymMemberDAO {

    private Connection connection;

    public GymMemberDAOImpl() {
    }

    @Override
    public void insert(GymMember member) throws AddGymMemberException {
        String insertPersonSql =
                "INSERT INTO person " +
                        "(first_name, last_name, birth_date, gender, email, phone, locker_number, username, password) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String insertGymMemberSql =
                "INSERT INTO gym_member " +
                        "(person_id, is_active, weight, height, enrollment) " +
                        "VALUES (?, ?, ?, ?, ?)";

        boolean previousAutoCommit = true;

        try {
            connection = getConnection();
            previousAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            int personId = insertPerson(member, insertPersonSql);
            insertGymMember(member, personId, insertGymMemberSql);

            connection.commit();

        } catch (SQLException exception) {
            rollback();
            throw new AddGymMemberException("database", "Erreur lors de l'ajout du membre.");
        } catch (AddGymMemberException exception) {
            rollback();
            throw exception;
        } finally {
            restoreAutoCommit(previousAutoCommit);
        }
    }

    @Override
    public List<GymMember> getAll() throws ReadGymMemberException {
        String sql =
                "SELECT p.id, p.first_name, p.last_name, p.birth_date, p.gender, " +
                        "       p.email, p.phone, p.locker_number, p.username, p.password, " +
                        "       gm.is_active, gm.weight, gm.height, " +
                        "       s.id AS subscription_id, s.tier AS subscription_tier, " +
                        "       s.price AS subscription_price, s.duration_months AS subscription_duration_months " +
                        "FROM person p " +
                        "INNER JOIN gym_member gm ON p.id = gm.person_id " +
                        "INNER JOIN subscription s ON gm.enrollment = s.id " +
                        "ORDER BY p.last_name, p.first_name";

        List<GymMember> members = new ArrayList<>();

        try {
            connection = getConnection();

            try (
                    PreparedStatement statement = connection.prepareStatement(sql);
                    ResultSet resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    members.add(mapResultSetToGymMember(resultSet));
                }
            }

            return members;

        } catch (SQLException exception) {
            throw new ReadGymMemberException(0, "Erreur lors de la récupération des membres.");
        }
    }

    @Override
    public GymMember getById(int id) throws ReadGymMemberException {
        String sql =
                "SELECT p.id, p.first_name, p.last_name, p.birth_date, p.gender, " +
                        "       p.email, p.phone, p.locker_number, p.username, p.password, " +
                        "       gm.is_active, gm.weight, gm.height, " +
                        "       s.id AS subscription_id, s.tier AS subscription_tier, " +
                        "       s.price AS subscription_price, s.duration_months AS subscription_duration_months " +
                        "FROM person p " +
                        "INNER JOIN gym_member gm ON p.id = gm.person_id " +
                        "INNER JOIN subscription s ON gm.enrollment = s.id " +
                        "WHERE p.id = ?";

        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapResultSetToGymMember(resultSet);
                    }

                    return null;
                }
            }

        } catch (SQLException exception) {
            throw new ReadGymMemberException(id, "Erreur lors de la récupération du membre.");
        }
    }

    @Override
    public void update(GymMember member) throws UpdateGymMemberException {
        String updatePersonSql =
                "UPDATE person " +
                        "SET first_name = ?, last_name = ?, birth_date = ?, gender = ?, " +
                        "    email = ?, phone = ?, locker_number = ?, username = ?, password = ? " +
                        "WHERE id = ?";

        String updateGymMemberSql =
                "UPDATE gym_member " +
                        "SET is_active = ?, weight = ?, height = ?, enrollment = ? " +
                        "WHERE person_id = ?";

        boolean previousAutoCommit = true;

        try {
            connection = getConnection();
            previousAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            updatePerson(member, updatePersonSql);
            updateGymMember(member, updateGymMemberSql);

            connection.commit();

        } catch (SQLException exception) {
            rollback();
            throw new UpdateGymMemberException(member.getId(), "Erreur lors de la modification du membre.");
        } catch (UpdateGymMemberException exception) {
            rollback();
            throw exception;
        } finally {
            restoreAutoCommit(previousAutoCommit);
        }
    }

    @Override
    public void delete(int id) throws DeleteGymMemberException {
        String deleteGymMemberSql =
                "DELETE FROM gym_member " +
                        "WHERE person_id = ?";

        String deletePersonSql =
                "DELETE FROM person " +
                        "WHERE id = ?";

        boolean previousAutoCommit = true;

        try {
            connection = getConnection();
            previousAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            deleteGymMember(id, deleteGymMemberSql);
            deletePerson(id, deletePersonSql);

            connection.commit();

        } catch (SQLException exception) {
            rollback();
            throw new DeleteGymMemberException(id, "Erreur lors de la suppression du membre.");
        } catch (DeleteGymMemberException exception) {
            rollback();
            throw exception;
        } finally {
            restoreAutoCommit(previousAutoCommit);
        }
    }

    private int insertPerson(GymMember member, String sql) throws SQLException, AddGymMemberException {
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, member.getFirstName());
            statement.setString(2, member.getLastName());
            statement.setDate(3, Date.valueOf(member.getBirthDate()));
            statement.setString(4, member.getGender().getDatabaseValue());
            statement.setString(5, member.getEmail());
            setNullableString(statement, 6, member.getPhone());
            setNullableInteger(statement, 7, member.getLockerNumber(), Types.SMALLINT);
            statement.setString(8, member.getUsername());
            statement.setString(9, member.getPassword());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new AddGymMemberException("person", "Aucune personne n'a été ajoutée.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new AddGymMemberException("person_id", "Impossible de récupérer l'identifiant de la personne créée.");
                }

                return generatedKeys.getInt(1);
            }
        }
    }

    private void insertGymMember(GymMember member, int personId, String sql) throws SQLException, AddGymMemberException {
        if (member.getEnrollment() == null) {
            throw new AddGymMemberException("enrollment", "L'abonnement du membre est obligatoire.");
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, personId);
            statement.setBoolean(2, member.getIsActive());
            statement.setDouble(3, member.getWeight());
            statement.setInt(4, member.getHeight());
            statement.setInt(5, member.getEnrollment().getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new AddGymMemberException("gym_member", "Aucun membre n'a été ajouté.");
            }
        }
    }

    private void updatePerson(GymMember member, String sql) throws SQLException, UpdateGymMemberException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, member.getFirstName());
            statement.setString(2, member.getLastName());
            statement.setDate(3, Date.valueOf(member.getBirthDate()));
            statement.setString(4, member.getGender().getDatabaseValue());
            statement.setString(5, member.getEmail());
            setNullableString(statement, 6, member.getPhone());
            setNullableInteger(statement, 7, member.getLockerNumber(), Types.SMALLINT);
            statement.setString(8, member.getUsername());
            statement.setString(9, member.getPassword());
            statement.setInt(10, member.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new UpdateGymMemberException(member.getId(), "Aucune personne n'a été modifiée.");
            }
        }
    }

    private void updateGymMember(GymMember member, String sql) throws SQLException, UpdateGymMemberException {
        if (member.getEnrollment() == null) {
            throw new UpdateGymMemberException(member.getId(), "L'abonnement du membre est obligatoire.");
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, member.getIsActive());
            statement.setDouble(2, member.getWeight());
            statement.setInt(3, member.getHeight());
            statement.setInt(4, member.getEnrollment().getId());
            statement.setInt(5, member.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new UpdateGymMemberException(member.getId(), "Aucun membre n'a été modifié.");
            }
        }
    }

    private void deleteGymMember(int id, String sql) throws SQLException, DeleteGymMemberException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new DeleteGymMemberException(id, "Aucun membre n'a été supprimé.");
            }
        }
    }

    private void deletePerson(int id, String sql) throws SQLException, DeleteGymMemberException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new DeleteGymMemberException(id, "Aucune personne n'a été supprimée.");
            }
        }
    }

    private GymMember mapResultSetToGymMember(ResultSet resultSet) throws SQLException, ReadGymMemberException {
        int id = resultSet.getInt("id");

        try {
            Subscription enrollment = new Subscription(
                    resultSet.getInt("subscription_id"),
                    resultSet.getInt("subscription_tier"),
                    resultSet.getDouble("subscription_price"),
                    resultSet.getInt("subscription_duration_months")
            );

            return new GymMember(
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
                    resultSet.getBoolean("is_active"),
                    resultSet.getDouble("weight"),
                    resultSet.getInt("height"),
                    enrollment
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
                InvalidWeightException |
                InvalidHeightException |
                InvalidPriceException |
                InvalidDurationException |
                IllegalArgumentException exception
        ) {
            throw new ReadGymMemberException(id, "Erreur lors de la création du membre à partir des données récupérées.");
        }
    }

    private Connection getConnection() throws SQLException {
        return SingletonConnection.getInstance();
    }

    private Integer getNullableInteger(ResultSet resultSet, String columnName) throws SQLException {
        int value = resultSet.getInt(columnName);

        if (resultSet.wasNull()) {
            return null;
        }

        return value;
    }

    private void setNullableString(PreparedStatement statement, int parameterIndex, String value) throws SQLException {
        if (value == null || value.isBlank()) {
            statement.setNull(parameterIndex, Types.VARCHAR);
        } else {
            statement.setString(parameterIndex, value);
        }
    }

    private void setNullableInteger(PreparedStatement statement, int parameterIndex, Integer value, int sqlType) throws SQLException {
        if (value == null) {
            statement.setNull(parameterIndex, sqlType);
        } else {
            statement.setInt(parameterIndex, value);
        }
    }

    private void rollback() {
        if (connection == null) {
            return;
        }

        try {
            connection.rollback();
        } catch (SQLException ignored) {
            // Aucun affichage dans la couche DataAccess.
        }
    }

    private void restoreAutoCommit(boolean previousAutoCommit) {
        if (connection == null) {
            return;
        }

        try {
            connection.setAutoCommit(previousAutoCommit);
        } catch (SQLException ignored) {
            // Aucun affichage dans la couche DataAccess.
        }
    }
}