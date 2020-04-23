package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.dao.RoomDAO;
import wooteco.chess.domain.room.Room;

import java.sql.SQLException;
import java.util.List;

@Service
public class RoomService {
    private static final RoomService ROOM_SERVICE = new RoomService();

    public static RoomService getInstance() {
        return ROOM_SERVICE;
    }

    public void addRoom(String roomName) throws SQLException {
        RoomDAO roomDAO = RoomDAO.getInstance();
        roomDAO.addRoom(roomName, "WHITE");
    }

    public void removeRoom(int roomId) throws SQLException {
        RoomDAO roomDAO = RoomDAO.getInstance();
        roomDAO.removeRoomById(roomId);
    }

    public Room findRoom(int roomId) throws SQLException {
        RoomDAO roomDAO = RoomDAO.getInstance();
        return roomDAO.findRoomById(roomId);
    }

    public List<Room> findAllRoom() throws SQLException {
        RoomDAO roomDAO = RoomDAO.getInstance();
        return roomDAO.findAllRoom();
    }
}
