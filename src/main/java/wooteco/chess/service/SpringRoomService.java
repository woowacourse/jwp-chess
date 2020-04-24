package wooteco.chess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooteco.chess.dao.RoomDAO;
import wooteco.chess.domain.room.Room;

import java.sql.SQLException;
import java.util.List;

@Service
public class SpringRoomService {

    @Autowired
    private RoomDAO roomDAO;

    public void addRoom(String roomName) throws SQLException {
        roomDAO.addRoom(roomName, "WHITE");
    }

    public void removeRoom(int roomId) throws SQLException {
        roomDAO.removeRoomById(roomId);
    }

    public List<Room> findAllRoom() throws SQLException {
        return roomDAO.findAllRoom();
    }
}
