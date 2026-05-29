package businessPackage;

import dataAccessPackage.roomDataAccess.*;
import exceptionPackage.room.*;
import modelPackage.Room;

import java.util.List;

public class RoomManager {

    private final IRoomDA roomDataAccess;

    public RoomManager() {
        this.roomDataAccess = new RoomDBAccess();
    }

    public RoomManager(IRoomDA roomDataAccess) {
        this.roomDataAccess = roomDataAccess;
    }

    public List<Room> getAllRooms() throws ReadRoomException {
        return roomDataAccess.getAll();
    }

    public Room getRoomById(int id) throws ReadRoomException {
        validateId(id);

        Room room = roomDataAccess.getById(id);

        if (room == null) {
            throw new ReadRoomException(
                    String.valueOf(id),
                    "Aucune salle trouvée avec cet identifiant."
            );
        }

        return room;
    }

    private void validateId(int id) throws ReadRoomException {
        if (id <= 0) {
            throw new ReadRoomException(
                    String.valueOf(id),
                    "L'identifiant de la salle doit être supérieur à 0."
            );
        }
    }
}