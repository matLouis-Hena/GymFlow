package dataAccessPackage.roomDataAccess;

import dataAccessPackage.SingletonConnection;
import exceptionPackage.room.ReadRoomException;
import exceptionPackage.validation.InvalidCapacityException;
import exceptionPackage.validation.InvalidNameException;
import modelPackage.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDBAccess implements IRoomDA {

    private static final String SELECT_ALL_ROOMS_SQL = """
            SELECT id, name, capacity
            FROM room
            ORDER BY name
            """;

    private static final String SELECT_ROOM_BY_ID_SQL = """
            SELECT id, name, capacity
            FROM room
            WHERE id = ?
            """;

    private Connection connection;

    public RoomDBAccess() {
    }

    @Override
    public List<Room> getAll() throws ReadRoomException {
        List<Room> rooms = new ArrayList<>();

        try {
            connection = getConnection();

            try (
                    PreparedStatement statement = connection.prepareStatement(SELECT_ALL_ROOMS_SQL);
                    ResultSet resultSet = statement.executeQuery()
            ) {
                while (resultSet.next()) {
                    rooms.add(mapRoom(resultSet));
                }
            }

            return rooms;

        } catch (SQLException exception) {
            throw new ReadRoomException(
                    "database",
                    "Erreur lors de la récupération des salles."
            );
        }
    }

    @Override
    public Room getById(int id) throws ReadRoomException {
        try {
            connection = getConnection();

            try (PreparedStatement statement = connection.prepareStatement(SELECT_ROOM_BY_ID_SQL)) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapRoom(resultSet);
                    }

                    return null;
                }
            }

        } catch (SQLException exception) {
            throw new ReadRoomException(
                    String.valueOf(id),
                    "Erreur lors de la récupération de la salle."
            );
        }
    }

    private Room mapRoom(ResultSet resultSet) throws SQLException, ReadRoomException {
        int id = resultSet.getInt("id");

        try {
            return new Room(
                    id,
                    resultSet.getString("name"),
                    resultSet.getInt("capacity")
            );

        } catch (InvalidCapacityException | InvalidNameException exception) {
            throw new ReadRoomException(
                    String.valueOf(id),
                    "Erreur lors de la création de la salle à partir des données récupérées."
            );
        }
    }

    private Connection getConnection() throws SQLException {
        return SingletonConnection.getInstance();
    }
}