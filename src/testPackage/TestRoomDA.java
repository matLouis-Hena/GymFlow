package testPackage;

import dataAccessPackage.roomDataAccess.IRoomDA;
import dataAccessPackage.roomDataAccess.RoomDBAccess;
import exceptionPackage.room.ReadRoomException;
import modelPackage.Room;

import java.util.List;

public class TestRoomDA {

    public static void main(String[] args) {
        IRoomDA roomDataAccess = new RoomDBAccess();

        try {
            List<Room> rooms = roomDataAccess.getAll();

            System.out.println("Salles récupérées : " + rooms.size());

            for (Room room : rooms) {
                System.out.println(
                        room.getId()
                                + " - "
                                + room.getName()
                                + " - capacité : "
                                + room.getCapacity()
                );
            }

            if (!rooms.isEmpty()) {
                Room room = roomDataAccess.getById(rooms.getFirst().getId());

                System.out.println(
                        "Salle récupérée : "
                                + room.getName()
                );
            }

        } catch (ReadRoomException exception) {
            System.out.println("Erreur pendant le test RoomDA : " + exception.getMessage());
        }
    }
}