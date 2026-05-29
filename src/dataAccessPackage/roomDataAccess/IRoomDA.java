package dataAccessPackage.roomDataAccess;

import exceptionPackage.room.ReadRoomException;
import modelPackage.Room;

import java.util.List;

public interface IRoomDA {

    List<Room> getAll() throws ReadRoomException;

    Room getById(int id) throws ReadRoomException;
}