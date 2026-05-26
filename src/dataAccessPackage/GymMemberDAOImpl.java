package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.Gender;
import modelPackage.GymMember;
import modelPackage.Subscription;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GymMemberDAOImpl implements GymMemberDAO {

    private static final String INSERT_GYM_MEMBER_SQL = """
        INSERT INTO gym_member
        (person_id, is_active, weight, height, enrollment)
        VALUES (?, ?, ?, ?, ?)
    """;

    private static final String SELECT_GYM_MEMBER_BY_ID_SQL = """
        SELECT p.*, gm.is_active, gm.weight, gm.height, gm.enrollment
        FROM gym_member gm
        INNER JOIN person p ON gm.person_id = p.id
        WHERE gm.person_id = ?
    """;

    private static final String SELECT_ALL_GYM_MEMBERS_SQL = """
        SELECT p.*, gm.is_active, gm.weight, gm.height, gm.enrollment
        FROM gym_member gm
        INNER JOIN person p ON gm.person_id = p.id
    """;

    private static final String UPDATE_GYM_MEMBER_SQL = """
        UPDATE gym_member
        SET is_active = ?,
            weight = ?,
            height = ?,
            enrollment = ?
        WHERE person_id = ?
    """;

    private static final String DELETE_GYM_MEMBER_SQL =
            "DELETE FROM gym_member WHERE person_id = ?";

    private final Connection connection;
    private final PersonDAO personDAO;

    public GymMemberDAOImpl() throws SQLException {
        connection = DatabaseConnection.getInstance();
        personDAO = new PersonDAOImpl();
    }

    @Override
    public void insert(GymMember member) throws DataAccessException {
        try {
            connection.setAutoCommit(false);

            int personId = personDAO.insert(member);

            try (PreparedStatement stmt = connection.prepareStatement(INSERT_GYM_MEMBER_SQL)) {
                stmt.setInt(1, personId);
                fillGymMemberInsertOrUpdateStatement(stmt, member, 2);
                stmt.executeUpdate();
            }

            connection.commit();

        } catch (Exception e) {
            rollback();
            throw new DataAccessException("Erreur lors de l'insertion du membre", e);

        } finally {
            restoreAutoCommit();
        }
    }

    @Override
    public GymMember getById(int id) throws DataAccessException {
        try (PreparedStatement stmt = connection.prepareStatement(SELECT_GYM_MEMBER_BY_ID_SQL)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapGymMember(rs);
                }
            }

            return null;

        } catch (Exception e) {
            throw new DataAccessException("Erreur lors de la récupération du membre", e);
        }
    }

    @Override
    public List<GymMember> getAll() throws DataAccessException {
        List<GymMember> members = new ArrayList<>();

        try (
                PreparedStatement stmt = connection.prepareStatement(SELECT_ALL_GYM_MEMBERS_SQL);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                members.add(mapGymMember(rs));
            }

            return members;

        } catch (Exception e) {
            throw new DataAccessException("Erreur lors de la récupération des membres", e);
        }
    }

    @Override
    public void update(GymMember member) throws DataAccessException {
        try {
            connection.setAutoCommit(false);

            personDAO.update(member);

            try (PreparedStatement stmt = connection.prepareStatement(UPDATE_GYM_MEMBER_SQL)) {
                fillGymMemberInsertOrUpdateStatement(stmt, member, 1);
                stmt.setInt(5, member.getId());
                stmt.executeUpdate();
            }

            connection.commit();

        } catch (Exception e) {
            rollback();
            throw new DataAccessException("Erreur lors de la mise à jour du membre", e);

        } finally {
            restoreAutoCommit();
        }
    }

    @Override
    public void delete(int id) throws DataAccessException {
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmt = connection.prepareStatement(DELETE_GYM_MEMBER_SQL)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            personDAO.delete(id);

            connection.commit();

        } catch (Exception e) {
            rollback();
            throw new DataAccessException("Erreur lors de la suppression du membre", e);

        } finally {
            restoreAutoCommit();
        }
    }

    private void fillGymMemberInsertOrUpdateStatement(
            PreparedStatement stmt,
            GymMember member,
            int startIndex
    ) throws SQLException {
        stmt.setBoolean(startIndex, member.getIsActive());
        stmt.setDouble(startIndex + 1, member.getWeight());
        stmt.setInt(startIndex + 2, member.getHeight());

        if (member.getEnrollment() != null) {
            stmt.setInt(startIndex + 3, member.getEnrollment().getId());
        } else {
            stmt.setNull(startIndex + 3, Types.INTEGER);
        }
    }

    private GymMember mapGymMember(ResultSet rs) throws Exception {
        Subscription enrollment = null;

        return new GymMember(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getDate("birth_date").toLocalDate(),
                Gender.fromDatabaseValue(rs.getString("gender")),
                rs.getString("email"),
                rs.getString("phone"),
                (Integer) rs.getObject("locker_number"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getBoolean("is_active"),
                rs.getDouble("weight"),
                rs.getInt("height"),
                enrollment
        );
    }

    private void rollback() throws DataAccessException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du rollback", e);
        }
    }

    private void restoreAutoCommit() throws DataAccessException {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la restauration de l'auto-commit", e);
        }
    }
}