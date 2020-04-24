package wooteco.chess.service;

import wooteco.chess.dao.SparkRoomDAO;
import wooteco.chess.domain.room.Room;

import java.sql.SQLException;
import java.util.List;

public class SparkRoomService {
    private static final SparkRoomService ROOM_SERVICE = new SparkRoomService();

    public static SparkRoomService getInstance() {
        return ROOM_SERVICE;
    }

    public void addRoom(String roomName) throws SQLException {
        SparkRoomDAO roomDAO = SparkRoomDAO.getInstance();
        roomDAO.addRoom(roomName, "WHITE");
    }

    public void removeRoom(int roomId) throws SQLException {
        SparkRoomDAO roomDAO = SparkRoomDAO.getInstance();
        roomDAO.removeRoomById(roomId);
    }

    public List<Room> findAllRoom() throws SQLException {
        SparkRoomDAO roomDAO = SparkRoomDAO.getInstance();
        List<Room> rooms = roomDAO.findAllRoom();
        return rooms;
    }
}
