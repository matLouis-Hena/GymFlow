package dataAccessPackage.appointmentDataAccess;

import dataAccessPackage.SingletonConnection;
import dataAccessPackage.coachAvailabilityDataAccess.CoachAvailabilityDBAccess;
import dataAccessPackage.coachAvailabilityDataAccess.ICoachAvailabilityDA;
import dataAccessPackage.gymMemberDataAccess.GymMemberDBAccess;
import dataAccessPackage.gymMemberDataAccess.IGymMemberDA;
import dataAccessPackage.roomDataAccess.IRoomDA;
import dataAccessPackage.roomDataAccess.RoomDBAccess;
import exceptionPackage.appointment.AddAppointmentException;
import exceptionPackage.appointment.DeleteAppointmentException;
import exceptionPackage.appointment.ReadAppointmentException;
import exceptionPackage.appointment.UpdateAppointmentException;
import exceptionPackage.coachAvailability.ReadCoachAvailabilityException;
import exceptionPackage.gymMember.ReadGymMemberException;
import exceptionPackage.room.ReadRoomException;
import modelPackage.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDBAccess implements IAppointmentDA {

    private static final String INSERT_APPOINTMENT_SQL = """
            INSERT INTO appointment
            (member_id, availability_id, objective, room_id, status, cancellation_reason)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    private static final String SELECT_APPOINTMENT_BY_ID_SQL = """
            SELECT id, member_id, availability_id, objective, room_id, status, cancellation_reason
            FROM appointment
            WHERE id = ?
            """;

    private static final String SELECT_ALL_APPOINTMENTS_SQL = """
            SELECT a.id, a.member_id, a.availability_id, a.objective, a.room_id, a.status, a.cancellation_reason
            FROM appointment a
            INNER JOIN coach_availability ca ON a.availability_id = ca.id
            ORDER BY ca.available_date, ca.start_time, a.id
            """;

    private static final String SELECT_APPOINTMENTS_BY_MEMBER_ID_SQL = """
            SELECT id, member_id, availability_id, objective, room_id, status, cancellation_reason
            FROM appointment
            WHERE member_id = ?
            ORDER BY availability_id
            """;

    private static final String EXISTS_FOR_MEMBER_ON_DATE_SQL = """
            SELECT COUNT(*) AS appointment_count
            FROM appointment a
            INNER JOIN coach_availability ca ON a.availability_id = ca.id
            WHERE a.member_id = ?
              AND ca.available_date = ?
              AND a.status NOT IN (?, ?, ?)
            """;

    private static final String DELETE_APPOINTMENT_SQL = """
            DELETE FROM appointment
            WHERE id = ?
            """;

    private static final String DELETE_APPOINTMENTS_BY_MEMBER_ID_SQL = """
            DELETE FROM appointment
            WHERE member_id = ?
            """;

    private final IGymMemberDA gymMemberDataAccess;
    private final ICoachAvailabilityDA coachAvailabilityDataAccess;
    private final IRoomDA roomDataAccess;

    private Connection connection;

    public AppointmentDBAccess() {
        this.gymMemberDataAccess = new GymMemberDBAccess();
        this.coachAvailabilityDataAccess = new CoachAvailabilityDBAccess();
        this.roomDataAccess = new RoomDBAccess();
    }

    @Override
    public int insert(Appointment appointment) throws AddAppointmentException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(
                    INSERT_APPOINTMENT_SQL,
                    Statement.RETURN_GENERATED_KEYS
            )) {
                fillInsertStatement(statement, appointment);

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new AddAppointmentException(
                            "appointment",
                            "Aucun rendez-vous n'a été ajouté."
                    );
                }

                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (!generatedKeys.next()) {
                        throw new AddAppointmentException(
                                "appointment_id",
                                "Impossible de récupérer l'identifiant du rendez-vous créé."
                        );
                    }

                    int generatedId = generatedKeys.getInt(1);
                    appointment.setId(generatedId);

                    return generatedId;
                }
            }

        } catch (SQLException exception) {
            throw new AddAppointmentException(
                    "database",
                    "Erreur lors de l'ajout du rendez-vous."
            );
        }
    }

    @Override
    public Appointment getById(int id) throws ReadAppointmentException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SELECT_APPOINTMENT_BY_ID_SQL)) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapAppointment(resultSet);
                    }

                    return null;
                }
            }

        } catch (SQLException exception) {
            throw new ReadAppointmentException(
                    String.valueOf(id),
                    "Erreur lors de la récupération du rendez-vous."
            );
        }
    }

    @Override
    public List<Appointment> getAll() throws ReadAppointmentException {
        List<Appointment> appointments = new ArrayList<>();

        try {
            connection = getConnection();

            try (
                    PreparedStatement statement = connection.prepareStatement(SELECT_ALL_APPOINTMENTS_SQL);
                    ResultSet resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    appointments.add(mapAppointment(resultSet));
                }
            }

            return appointments;

        } catch (SQLException exception) {
            throw new ReadAppointmentException(
                    "database",
                    "Erreur lors de la recuperation des rendez-vous."
            );
        }
    }

    @Override
    public List<Appointment> getByMemberId(int memberId) throws ReadAppointmentException {
        List<Appointment> appointments = new ArrayList<>();

        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SELECT_APPOINTMENTS_BY_MEMBER_ID_SQL)) {
                statement.setInt(1, memberId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        appointments.add(mapAppointment(resultSet));
                    }
                }
            }

            return appointments;

        } catch (SQLException exception) {
            throw new ReadAppointmentException(
                    String.valueOf(memberId),
                    "Erreur lors de la récupération des rendez-vous du membre."
            );
        }
    }

    @Override
    public List<Appointment> getByCoachId(int coachId) throws ReadAppointmentException {
        List<Appointment> appointments = new ArrayList<>();

        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SELECT_APPOINTMENTS_BY_COACH_ID_SQL)) {
                statement.setInt(1, coachId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        appointments.add(mapAppointment(resultSet));
                    }
                }
            }

            return appointments;

        } catch (SQLException exception) {
            throw new ReadAppointmentException(
                    String.valueOf(coachId),
                    "Erreur lors de la récupération des rendez-vous du coach."
            );
        }
    }

    @Override
    public boolean existsForMemberOnDate(int memberId, LocalDate date) throws ReadAppointmentException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(EXISTS_FOR_MEMBER_ON_DATE_SQL)) {
                statement.setInt(1, memberId);
                statement.setDate(2, Date.valueOf(date));
                statement.setInt(3, AppointmentStatus.CANCELLED_BY_COACH.ordinal());
                statement.setInt(4, AppointmentStatus.CANCELLED_BY_MEMBER.ordinal());
                statement.setInt(5, AppointmentStatus.CANCELLED_BY_ADMIN.ordinal());

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("appointment_count") > 0;
                    }

                    return false;
                }
            }

        } catch (SQLException exception) {
            throw new ReadAppointmentException(
                    String.valueOf(memberId),
                    "Erreur lors de la vérification des rendez-vous du membre."
            );
        }
    }

    @Override
    public void updatePastConfirmedAppointments() throws UpdateAppointmentException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(UPDATE_PAST_CONFIRMED_APPOINTMENTS_SQL)) {
                statement.setInt(1, AppointmentStatus.DONE.ordinal());
                statement.setInt(2, AppointmentStatus.CONFIRMED.ordinal());
                statement.executeUpdate();
            }

        } catch (SQLException exception) {
            throw new UpdateAppointmentException(
                    "pastAppointments",
                    "Erreur lors de la mise a jour des rendez-vous passes."
            );
        }
    }

    @Override
    public void delete(int id) throws DeleteAppointmentException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(DELETE_APPOINTMENT_SQL)) {
                statement.setInt(1, id);

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DeleteAppointmentException(
                            String.valueOf(id),
                            "Aucun rendez-vous n'a été supprimé."
                    );
                }
            }

        } catch (SQLException exception) {
            throw new DeleteAppointmentException(
                    String.valueOf(id),
                    "Erreur lors de la suppression du rendez-vous."
            );
        }
    }

    @Override
    public void deleteByMemberId(int memberId) throws DeleteAppointmentException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(DELETE_APPOINTMENTS_BY_MEMBER_ID_SQL)) {
                statement.setInt(1, memberId);
                statement.executeUpdate();
            }

        } catch (SQLException exception) {
            throw new DeleteAppointmentException(
                    String.valueOf(memberId),
                    "Erreur lors de la suppression des rendez-vous du membre."
            );
        }
    }

    private void fillInsertStatement(PreparedStatement statement, Appointment appointment)
            throws SQLException, AddAppointmentException {
        if (appointment.getMember() == null) {
            throw new AddAppointmentException(
                    "member",
                    "Le membre du rendez-vous est obligatoire."
            );
        }

        if (appointment.getAvailability() == null) {
            throw new AddAppointmentException(
                    "availability",
                    "La disponibilité du rendez-vous est obligatoire."
            );
        }

        if (appointment.getStatus() == null) {
            throw new AddAppointmentException(
                    "status",
                    "Le statut du rendez-vous est obligatoire."
            );
        }

        statement.setInt(1, appointment.getMember().getId());
        statement.setInt(2, appointment.getAvailability().getId());
        setNullableString(statement, 3, appointment.getObjective());

        if (appointment.getRoom() == null) {
            statement.setNull(4, Types.TINYINT);
        } else {
            statement.setInt(4, appointment.getRoom().getId());
        }

        statement.setInt(5, appointment.getStatus().ordinal());
        setNullableString(statement, 6, appointment.getCancellationReason());
    }

    private Appointment mapAppointment(ResultSet resultSet) throws SQLException, ReadAppointmentException {
        int appointmentId = resultSet.getInt("id");

        try {
            int memberId = resultSet.getInt("member_id");
            int availabilityId = resultSet.getInt("availability_id");

            int roomId = resultSet.getInt("room_id");
            Room room = null;

            if (!resultSet.wasNull()) {
                room = roomDataAccess.getById(roomId);
            }

            GymMember member = gymMemberDataAccess.getById(memberId);
            CoachAvailability availability = coachAvailabilityDataAccess.getById(availabilityId);
            AppointmentStatus status = getAppointmentStatus(resultSet.getInt("status"));

            if (member == null) {
                throw new ReadAppointmentException(
                        String.valueOf(memberId),
                        "Le membre lié au rendez-vous est introuvable."
                );
            }

            if (availability == null) {
                throw new ReadAppointmentException(
                        String.valueOf(availabilityId),
                        "La disponibilité liée au rendez-vous est introuvable."
                );
            }

            return new Appointment(
                    appointmentId,
                    member,
                    availability,
                    resultSet.getString("objective"),
                    room,
                    status,
                    resultSet.getString("cancellation_reason")
            );

        } catch (
                ReadGymMemberException |
                ReadCoachAvailabilityException |
                ReadRoomException |
                IllegalArgumentException exception
        ) {
            throw new ReadAppointmentException(
                    String.valueOf(appointmentId),
                    "Erreur lors de la création du rendez-vous à partir des données récupérées."
            );
        }
    }

    private AppointmentStatus getAppointmentStatus(int statusIndex) {
        AppointmentStatus[] statuses = AppointmentStatus.values();

        if (statusIndex < 0 || statusIndex >= statuses.length) {
            throw new IllegalArgumentException("Statut de rendez-vous invalide.");
        }

        return statuses[statusIndex];
    }

    private void setNullableString(PreparedStatement statement, int parameterIndex, String value)
            throws SQLException {
        if (value == null || value.isBlank()) {
            statement.setNull(parameterIndex, Types.VARCHAR);
        } else {
            statement.setString(parameterIndex, value);
        }
    }

    private Connection getConnection() throws SQLException {
        return SingletonConnection.getInstance();
    }

    @Override
    public void updateStatus(int id, int status, String cancellationReason)
            throws UpdateAppointmentException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(UPDATE_APPOINTMENT_STATUS_SQL)) {
                statement.setInt(1, status);
                setNullableString(statement, 2, cancellationReason);
                statement.setInt(3, id);

                int affectedRows = statement.executeUpdate();

                if (affectedRows == 0) {
                    throw new UpdateAppointmentException(
                            String.valueOf(id),
                            "Aucun rendez-vous n'a été modifié."
                    );
                }
            }

        } catch (SQLException exception) {
            throw new UpdateAppointmentException(
                    String.valueOf(id),
                    "Erreur lors de la modification du statut du rendez-vous."
            );
        }
    }

    private static final String UPDATE_APPOINTMENT_STATUS_SQL = """
        UPDATE appointment
        SET status = ?,
            cancellation_reason = ?
        WHERE id = ?
        """;

    private static final String UPDATE_PAST_CONFIRMED_APPOINTMENTS_SQL = """
        UPDATE appointment a
        INNER JOIN coach_availability ca ON a.availability_id = ca.id
        SET a.status = ?,
            a.cancellation_reason = NULL
        WHERE a.status = ?
          AND (
                ca.available_date < CURDATE()
                OR (ca.available_date = CURDATE() AND ca.end_time < CURTIME())
          )
        """;

    private static final String SELECT_APPOINTMENTS_BY_COACH_ID_SQL = """
        SELECT a.id,
               a.member_id,
               a.availability_id,
               a.objective,
               a.room_id,
               a.status,
               a.cancellation_reason
        FROM appointment a
        INNER JOIN coach_availability ca ON a.availability_id = ca.id
        WHERE ca.person_id = ?
        ORDER BY ca.available_date, ca.start_time
        """;
}
