package dataAccessPackage.sponsorshipDataAccess;

import dataAccessPackage.SingletonConnection;
import dataAccessPackage.gymMemberDataAccess.*;
import exceptionPackage.gymMember.*;
import exceptionPackage.sponsorship.*;
import modelPackage.GymMember;
import modelPackage.Sponsorship;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SponsorshipDBAccess implements ISponsorshipDA {

    private static final String INSERT_SPONSORSHIP_SQL = """
            INSERT INTO sponsorship
            (sponsor_id, sponsored_id, start_date)
            VALUES (?, ?, CURDATE())
            """;

    private static final String SELECT_SPONSORSHIPS_BY_MEMBER_ID_SQL = """
            SELECT id, start_date, sponsor_id, sponsored_id
            FROM sponsorship
            WHERE sponsor_id = ? OR sponsored_id = ?
            ORDER BY start_date DESC, id DESC
            """;

    private static final String COUNT_SPONSORED_BY_SPONSOR_ID_SQL = """
            SELECT COUNT(*) AS sponsored_count
            FROM sponsorship
            WHERE sponsor_id = ?
            """;

    private static final String EXISTS_FOR_SPONSORED_ID_SQL = """
            SELECT COUNT(*) AS sponsorship_count
            FROM sponsorship
            WHERE sponsored_id = ?
            """;

    private static final String DELETE_SPONSORSHIPS_BY_MEMBER_ID_SQL = """
            DELETE FROM sponsorship
            WHERE sponsor_id = ? OR sponsored_id = ?
            """;

    private final IGymMemberDA gymMemberDataAccess;
    private Connection connection;

    public SponsorshipDBAccess() {
        this.gymMemberDataAccess = new GymMemberDBAccess();
    }

    @Override
    public void insert(int sponsorId, int sponsoredId) throws AddSponsorshipException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(INSERT_SPONSORSHIP_SQL)) {
                statement.setInt(1, sponsorId);
                statement.setInt(2, sponsoredId);

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new AddSponsorshipException(
                            String.valueOf(sponsoredId),
                            "Aucun parrainage n'a ete ajoute."
                    );
                }
            }

        } catch (SQLException exception) {
            throw new AddSponsorshipException(
                    String.valueOf(sponsoredId),
                    "Erreur lors de l'ajout du parrainage."
            );
        }
    }

    @Override
    public List<Sponsorship> getByMemberId(int memberId) throws ReadSponsorshipException {
        List<Sponsorship> sponsorships = new ArrayList<>();

        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SELECT_SPONSORSHIPS_BY_MEMBER_ID_SQL)) {
                statement.setInt(1, memberId);
                statement.setInt(2, memberId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        sponsorships.add(mapSponsorship(resultSet));
                    }
                }
            }

            return sponsorships;

        } catch (SQLException exception) {
            throw new ReadSponsorshipException(
                    String.valueOf(memberId),
                    "Erreur lors de la récupération des parrainages du membre."
            );
        }
    }

    @Override
    public int countSponsoredBySponsorId(int sponsorId) throws ReadSponsorshipException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(COUNT_SPONSORED_BY_SPONSOR_ID_SQL)) {
                statement.setInt(1, sponsorId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("sponsored_count");
                    }

                    return 0;
                }
            }

        } catch (SQLException exception) {
            throw new ReadSponsorshipException(
                    String.valueOf(sponsorId),
                    "Erreur lors du comptage des filleuls du membre."
            );
        }
    }

    @Override
    public boolean existsForSponsoredId(int sponsoredId) throws ReadSponsorshipException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(EXISTS_FOR_SPONSORED_ID_SQL)) {
                statement.setInt(1, sponsoredId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("sponsorship_count") > 0;
                    }

                    return false;
                }
            }

        } catch (SQLException exception) {
            throw new ReadSponsorshipException(
                    String.valueOf(sponsoredId),
                    "Erreur lors de la vérification du parrainage du membre."
            );
        }
    }

    @Override
    public void deleteByMemberId(int memberId) throws DeleteSponsorshipException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(DELETE_SPONSORSHIPS_BY_MEMBER_ID_SQL)) {
                statement.setInt(1, memberId);
                statement.setInt(2, memberId);
                statement.executeUpdate();
            }

        } catch (SQLException exception) {
            throw new DeleteSponsorshipException(
                    String.valueOf(memberId),
                    "Erreur lors de la suppression des parrainages du membre."
            );
        }
    }

    private Sponsorship mapSponsorship(ResultSet resultSet) throws SQLException, ReadSponsorshipException {
        int sponsorshipId = resultSet.getInt("id");

        try {
            int sponsorId = resultSet.getInt("sponsor_id");
            int sponsoredId = resultSet.getInt("sponsored_id");

            GymMember sponsor = gymMemberDataAccess.getById(sponsorId);
            GymMember sponsored = gymMemberDataAccess.getById(sponsoredId);

            if (sponsor == null) {
                throw new ReadSponsorshipException(
                        String.valueOf(sponsorId),
                        "Le parrain lié au parrainage est introuvable."
                );
            }

            if (sponsored == null) {
                throw new ReadSponsorshipException(
                        String.valueOf(sponsoredId),
                        "Le filleul lié au parrainage est introuvable."
                );
            }

            return new Sponsorship(
                    sponsorshipId,
                    resultSet.getDate("start_date").toLocalDate(),
                    sponsor,
                    sponsored
            );

        } catch (
                ReadGymMemberException |
                IllegalArgumentException exception
        ) {
            throw new ReadSponsorshipException(
                    String.valueOf(sponsorshipId),
                    "Erreur lors de la création du parrainage à partir des données récupérées."
            );
        }
    }

    private Connection getConnection() throws SQLException {
        return SingletonConnection.getInstance();
    }
}
