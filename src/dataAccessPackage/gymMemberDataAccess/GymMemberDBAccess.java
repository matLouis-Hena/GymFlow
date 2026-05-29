package dataAccessPackage.gymMemberDataAccess;

import dataAccessPackage.SingletonConnection;
import exceptionPackage.gymMember.*;
import exceptionPackage.validation.*;
import modelPackage.Gender;
import modelPackage.GymMember;
import modelPackage.Subscription;
import modelPackage.SubscriptionType;
import securityPackage.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GymMemberDBAccess implements IGymMemberDA {

    private Connection connection;

    public GymMemberDBAccess() {
    }

    @Override
    public void insert(GymMember member) throws AddGymMemberException, DuplicateGymMemberException {
        String insertPersonSql =
                "INSERT INTO person " +
                        "(first_name, last_name, birth_date, gender, email, phone, locker_number, username, password) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String insertSubscriptionSql =
                "INSERT INTO subscription " +
                        "(type, price, duration_months) " +
                        "VALUES (?, ?, ?)";

        String insertGymMemberSql =
                "INSERT INTO gym_member " +
                        "(person_id, is_active, weight, height, enrollment) " +
                        "VALUES (?, ?, ?, ?, ?)";

        boolean previousAutoCommit = true;

        try {
            connection = getConnection();
            previousAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            validateMemberBeforeInsert(member);
            checkEmailAndUsernameAvailabilityForInsert(member);

            int personId = insertPerson(member, insertPersonSql);
            int subscriptionId = insertSubscription(member.getEnrollment(), insertSubscriptionSql);
            insertGymMember(member, personId, subscriptionId, insertGymMemberSql);

            connection.commit();

        } catch (SQLIntegrityConstraintViolationException exception) {
            rollback();

            DuplicateGymMemberException duplicateException = buildDuplicateGymMemberException(exception);

            if (duplicateException != null) {
                throw duplicateException;
            }

            throw new AddGymMemberException(
                    "database",
                    "Erreur d'intégrité lors de l'ajout du membre."
            );

        } catch (SQLException exception) {
            rollback();
            throw new AddGymMemberException(
                    "database",
                    "Erreur lors de l'ajout du membre."
            );

        } catch (AddGymMemberException | DuplicateGymMemberException exception) {
            rollback();
            throw exception;

        } finally {
            restoreAutoCommit(previousAutoCommit);
        }
    }

    @Override
    public void insertForExistingPerson(GymMember member) throws AddGymMemberException, DuplicateGymMemberException {
        String updateLockerSql =
                "UPDATE person " +
                        "SET locker_number = ? " +
                        "WHERE id = ?";

        String insertSubscriptionSql =
                "INSERT INTO subscription " +
                        "(type, price, duration_months) " +
                        "VALUES (?, ?, ?)";

        String insertGymMemberSql =
                "INSERT INTO gym_member " +
                        "(person_id, is_active, weight, height, enrollment) " +
                        "VALUES (?, ?, ?, ?, ?)";

        boolean previousAutoCommit = true;

        try {
            connection = getConnection();
            previousAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            validateMemberBeforeInsert(member);
            checkPersonExists(member.getId());
            checkPersonIsNotAlreadyMember(member.getId());
            updateLockerNumber(member, updateLockerSql);

            int subscriptionId = insertSubscription(member.getEnrollment(), insertSubscriptionSql);
            insertGymMember(member, member.getId(), subscriptionId, insertGymMemberSql);

            connection.commit();

        } catch (SQLIntegrityConstraintViolationException exception) {
            rollback();
            throw new DuplicateGymMemberException(
                    "person_id",
                    "Ce compte est deja inscrit comme membre."
            );

        } catch (SQLException exception) {
            rollback();
            throw new AddGymMemberException(
                    "database",
                    "Erreur lors de l'inscription du compte existant."
            );

        } catch (AddGymMemberException | DuplicateGymMemberException exception) {
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
                        "       s.id AS subscription_id, s.type AS subscription_type, " +
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
            throw new ReadGymMemberException(
                    "database",
                    "Erreur lors de la récupération des membres."
            );
        }
    }

    @Override
    public GymMember getById(int id) throws ReadGymMemberException {
        String sql =
                "SELECT p.id, p.first_name, p.last_name, p.birth_date, p.gender, " +
                        "       p.email, p.phone, p.locker_number, p.username, p.password, " +
                        "       gm.is_active, gm.weight, gm.height, " +
                        "       s.id AS subscription_id, s.type AS subscription_type, " +
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
            throw new ReadGymMemberException(
                    String.valueOf(id),
                    "Erreur lors de la récupération du membre."
            );
        }
    }

    @Override
    public void update(GymMember member) throws UpdateGymMemberException, DuplicateGymMemberException {
        String updatePersonSql =
                "UPDATE person " +
                        "SET first_name = ?, last_name = ?, birth_date = ?, gender = ?, " +
                        "    email = ?, phone = ?, locker_number = ?, username = ?, password = ? " +
                        "WHERE id = ?";

        String updateSubscriptionSql =
                "UPDATE subscription " +
                        "SET type = ?, price = ?, duration_months = ? " +
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

            checkEmailAndUsernameAvailabilityForUpdate(member);

            updatePerson(member, updatePersonSql);
            updateSubscription(member.getEnrollment(), updateSubscriptionSql);
            updateGymMember(member, updateGymMemberSql);

            connection.commit();

        } catch (SQLIntegrityConstraintViolationException exception) {
            rollback();

            DuplicateGymMemberException duplicateException = buildDuplicateGymMemberException(exception);

            if (duplicateException != null) {
                throw duplicateException;
            }

            throw new UpdateGymMemberException(
                    "database",
                    "Erreur d'intégrité lors de la modification du membre."
            );

        } catch (SQLException exception) {
            rollback();
            throw new UpdateGymMemberException(
                    "database",
                    "Erreur lors de la modification du membre."
            );

        } catch (UpdateGymMemberException | DuplicateGymMemberException exception) {
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

            int subscriptionId = getSubscriptionIdByMemberId(id);

            deleteGymMember(id, deleteGymMemberSql);
            deletePerson(id, deletePersonSql);
            deleteSubscription(subscriptionId);

            connection.commit();

        } catch (SQLException exception) {
            rollback();
            throw new DeleteGymMemberException(
                    String.valueOf(id),
                    "Erreur lors de la suppression du membre."
            );

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
            statement.setString(9, PasswordUtil.hashIfNeeded(member.getPassword()));

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new AddGymMemberException(
                        "person",
                        "Aucune personne n'a été ajoutée."
                );
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new AddGymMemberException(
                            "person_id",
                            "Impossible de récupérer l'identifiant de la personne créée."
                    );
                }

                return generatedKeys.getInt(1);
            }
        }
    }

    private int insertSubscription(Subscription subscription, String sql)
            throws SQLException, AddGymMemberException {
        if (subscription == null) {
            throw new AddGymMemberException(
                    "enrollment",
                    "L'abonnement du membre est obligatoire."
            );
        }

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, subscription.getType().getDatabaseValue());
            statement.setDouble(2, subscription.getPrice());
            statement.setInt(3, subscription.getDurationMonths());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new AddGymMemberException(
                        "subscription",
                        "Aucun abonnement n'a été ajouté."
                );
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (!generatedKeys.next()) {
                    throw new AddGymMemberException(
                            "subscription_id",
                            "Impossible de récupérer l'identifiant de l'abonnement créé."
                    );
                }

                return generatedKeys.getInt(1);
            }
        }
    }

    private void insertGymMember(GymMember member, int personId, int subscriptionId, String sql)
            throws SQLException, AddGymMemberException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, personId);
            statement.setBoolean(2, member.getIsActive());
            statement.setDouble(3, member.getWeight());
            statement.setInt(4, member.getHeight());
            statement.setInt(5, subscriptionId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new AddGymMemberException(
                        "gym_member",
                        "Aucun membre n'a été ajouté."
                );
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
            statement.setString(9, PasswordUtil.hashIfNeeded(member.getPassword()));
            statement.setInt(10, member.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new UpdateGymMemberException(
                        String.valueOf(member.getId()),
                        "Aucune personne n'a été modifiée."
                );
            }
        }
    }

    private void updateLockerNumber(GymMember member, String sql) throws SQLException, AddGymMemberException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setNullableInteger(statement, 1, member.getLockerNumber(), Types.SMALLINT);
            statement.setInt(2, member.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new AddGymMemberException(
                        String.valueOf(member.getId()),
                        "Aucun compte n'a ete trouve pour ce membre."
                );
            }
        }
    }

    private void updateSubscription(Subscription subscription, String sql)
            throws SQLException, UpdateGymMemberException {
        if (subscription == null) {
            throw new UpdateGymMemberException(
                    "enrollment",
                    "L'abonnement du membre est obligatoire."
            );
        }

        if (subscription.getId() <= 0) {
            throw new UpdateGymMemberException(
                    String.valueOf(subscription.getId()),
                    "L'identifiant de l'abonnement est invalide."
            );
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, subscription.getType().getDatabaseValue());
            statement.setDouble(2, subscription.getPrice());
            statement.setInt(3, subscription.getDurationMonths());
            statement.setInt(4, subscription.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new UpdateGymMemberException(
                        String.valueOf(subscription.getId()),
                        "Aucun abonnement n'a été modifié."
                );
            }
        }
    }

    private void updateGymMember(GymMember member, String sql) throws SQLException, UpdateGymMemberException {
        if (member.getEnrollment() == null) {
            throw new UpdateGymMemberException(
                    String.valueOf(member.getId()),
                    "L'abonnement du membre est obligatoire."
            );
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, member.getIsActive());
            statement.setDouble(2, member.getWeight());
            statement.setInt(3, member.getHeight());
            statement.setInt(4, member.getEnrollment().getId());
            statement.setInt(5, member.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new UpdateGymMemberException(
                        String.valueOf(member.getId()),
                        "Aucun membre n'a été modifié."
                );
            }
        }
    }

    private void deleteGymMember(int id, String sql) throws SQLException, DeleteGymMemberException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new DeleteGymMemberException(
                        String.valueOf(id),
                        "Aucun membre n'a été supprimé."
                );
            }
        }
    }

    private void deletePerson(int id, String sql) throws SQLException, DeleteGymMemberException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new DeleteGymMemberException(
                        String.valueOf(id),
                        "Aucune personne n'a été supprimée."
                );
            }
        }
    }

    private void deleteSubscription(int subscriptionId)
            throws SQLException, DeleteGymMemberException {
        String sql =
                "DELETE FROM subscription " +
                        "WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, subscriptionId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new DeleteGymMemberException(
                        String.valueOf(subscriptionId),
                        "Aucun abonnement n'a été supprimé."
                );
            }
        }
    }

    private int getSubscriptionIdByMemberId(int memberId)
            throws SQLException, DeleteGymMemberException {
        String sql =
                "SELECT enrollment " +
                        "FROM gym_member " +
                        "WHERE person_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("enrollment");
                }

                throw new DeleteGymMemberException(
                        String.valueOf(memberId),
                        "Aucun abonnement lié au membre n'a été trouvé."
                );
            }
        }
    }

    private GymMember mapResultSetToGymMember(ResultSet resultSet) throws SQLException, ReadGymMemberException {
        int id = resultSet.getInt("id");

        try {
            Subscription enrollment = new Subscription(
                    resultSet.getInt("subscription_id"),
                    SubscriptionType.fromDatabaseValue(resultSet.getString("subscription_type")),
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

            System.out.println("Erreur creation membre id " + id);
            System.out.println("Type erreur : " + exception.getClass().getSimpleName());
            System.out.println("Message : " + exception.getMessage());

            throw new ReadGymMemberException(
                    String.valueOf(id),
                    "Erreur lors de la création du membre à partir des données récupérées."
            );
        }
    }

    private void validateMemberBeforeInsert(GymMember member) throws AddGymMemberException {
        if (member.getEnrollment() == null) {
            throw new AddGymMemberException(
                    "enrollment",
                    "L'abonnement du membre est obligatoire."
            );
        }

        if (member.getEnrollment().getDurationMonths() <= 0) {
            throw new AddGymMemberException(
                    "durationMonths",
                    "La durée de l'abonnement doit être supérieure à 0."
            );
        }
    }

    private void checkEmailAndUsernameAvailabilityForInsert(GymMember member)
            throws SQLException, DuplicateGymMemberException {
        String sql =
                "SELECT email, username " +
                        "FROM person " +
                        "WHERE email = ? OR username = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, member.getEmail());
            statement.setString(2, member.getUsername());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String existingEmail = resultSet.getString("email");
                    String existingUsername = resultSet.getString("username");

                    if (existingEmail != null && existingEmail.equals(member.getEmail())) {
                        throw new DuplicateGymMemberException(
                                "email",
                                "Cette adresse email est déjà utilisée."
                        );
                    }

                    if (existingUsername != null && existingUsername.equals(member.getUsername())) {
                        throw new DuplicateGymMemberException(
                                "username",
                                "Ce nom d'utilisateur est déjà utilisé."
                        );
                    }
                }
            }
        }
    }

    private void checkPersonExists(int personId) throws SQLException, AddGymMemberException {
        String sql =
                "SELECT id " +
                        "FROM person " +
                        "WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, personId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new AddGymMemberException(
                            String.valueOf(personId),
                            "Le compte selectionne n'existe pas."
                    );
                }
            }
        }
    }

    private void checkPersonIsNotAlreadyMember(int personId)
            throws SQLException, DuplicateGymMemberException {
        String sql =
                "SELECT person_id " +
                        "FROM gym_member " +
                        "WHERE person_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, personId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    throw new DuplicateGymMemberException(
                            String.valueOf(personId),
                            "Ce compte est deja inscrit comme membre."
                    );
                }
            }
        }
    }

    private void checkEmailAndUsernameAvailabilityForUpdate(GymMember member)
            throws SQLException, DuplicateGymMemberException {
        String sql =
                "SELECT email, username " +
                        "FROM person " +
                        "WHERE (email = ? OR username = ?) AND id <> ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, member.getEmail());
            statement.setString(2, member.getUsername());
            statement.setInt(3, member.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String existingEmail = resultSet.getString("email");
                    String existingUsername = resultSet.getString("username");

                    if (existingEmail != null && existingEmail.equals(member.getEmail())) {
                        throw new DuplicateGymMemberException(
                                "email",
                                "Cette adresse email est déjà utilisée."
                        );
                    }

                    if (existingUsername != null && existingUsername.equals(member.getUsername())) {
                        throw new DuplicateGymMemberException(
                                "username",
                                "Ce nom d'utilisateur est déjà utilisé."
                        );
                    }
                }
            }
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

    private DuplicateGymMemberException buildDuplicateGymMemberException(
            SQLIntegrityConstraintViolationException exception
    ) {
        String sqlMessage = exception.getMessage();

        if (sqlMessage == null) {
            return null;
        }

        String lowerMessage = sqlMessage.toLowerCase();

        if (lowerMessage.contains("email")) {
            return new DuplicateGymMemberException(
                    "email",
                    "Cette adresse email est déjà utilisée."
            );
        }

        if (lowerMessage.contains("username")) {
            return new DuplicateGymMemberException(
                    "username",
                    "Ce nom d'utilisateur est déjà utilisé."
            );
        }

        return null;
    }
}
