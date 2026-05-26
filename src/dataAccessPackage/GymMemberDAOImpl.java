package dataAccessPackage;

import exceptionPackage.DataAccessException;
import modelPackage.Gender;
import modelPackage.GymMember;
import modelPackage.Person;
import modelPackage.Subscription;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GymMemberDAOImpl implements GymMemberDAO {

    private Connection connection;
    private PersonDAO personDAO;

    public GymMemberDAOImpl() throws SQLException {
        connection = DatabaseConnection.getInstance();
        personDAO = new PersonDAOImpl();
    }

    @Override
    public void insert(GymMember member) throws DataAccessException {

        String sql = """
            INSERT INTO gym_member
            (person_id, is_active, weight, height, enrollment)
            VALUES (?, ?, ?, ?, ?)
        """;

        try {

            int generatedPersonId = personDAO.insert(member);

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {

                stmt.setInt(1, generatedPersonId);

                stmt.setBoolean(2, member.getIsActive());

                stmt.setDouble(3, member.getWeight());

                stmt.setInt(4, member.getHeight());

                if(member.getEnrollment() != null) {
                    stmt.setInt(5, member.getEnrollment().getId());
                } else {
                    stmt.setNull(5, Types.INTEGER);
                }

                stmt.executeUpdate();
            }

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Erreur lors de l'insertion du membre",
                    e
            );
        }
    }

    @Override
    public GymMember getById(int id) throws DataAccessException {

        String sql = """
            SELECT *
            FROM gym_member gm
            INNER JOIN person p
            ON gm.person_id = p.id
            WHERE p.id = ?
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {

                Subscription enrollment = null;

                return new GymMember(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("birth_date").toLocalDate(),
                        Gender.fromDatabaseValue(
                                rs.getString("gender")
                        ),
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

            return null;

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Erreur lors de la récupération du membre",
                    e
            );

        } catch (Exception e) {

            throw new DataAccessException(
                    "Erreur lors du mapping du membre",
                    e
            );
        }
    }

    @Override
    public List<GymMember> getAll() throws DataAccessException {

        List<GymMember> members = new ArrayList<>();

        String sql = """
            SELECT *
            FROM gym_member gm
            INNER JOIN person p
            ON gm.person_id = p.id
        """;

        try (
                PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while(rs.next()) {

                Subscription enrollment = null;

                GymMember member = new GymMember(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("birth_date").toLocalDate(),
                        Gender.fromDatabaseValue(
                                rs.getString("gender")
                        ),
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

                members.add(member);
            }

            return members;

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Erreur lors de la récupération des membres",
                    e
            );

        } catch (Exception e) {

            throw new DataAccessException(
                    "Erreur lors du mapping des membres",
                    e
            );
        }
    }

    @Override
    public void update(GymMember member) throws DataAccessException {

        String sql = """
            UPDATE gym_member
            SET is_active = ?,
                weight = ?,
                height = ?,
                enrollment = ?
            WHERE person_id = ?
        """;

        try {

            personDAO.update(member);

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {

                stmt.setBoolean(1, member.getIsActive());

                stmt.setDouble(2, member.getWeight());

                stmt.setInt(3, member.getHeight());

                if(member.getEnrollment() != null) {
                    stmt.setInt(4, member.getEnrollment().getId());
                } else {
                    stmt.setNull(4, Types.INTEGER);
                }

                stmt.setInt(5, member.getId());

                stmt.executeUpdate();
            }

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Erreur lors de la mise à jour du membre",
                    e
            );
        }
    }

    @Override
    public void delete(int id) throws DataAccessException {

        String sql = "DELETE FROM gym_member WHERE person_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();

            personDAO.delete(id);

        } catch (SQLException e) {

            throw new DataAccessException(
                    "Erreur lors de la suppression du membre",
                    e
            );
        }
    }
}