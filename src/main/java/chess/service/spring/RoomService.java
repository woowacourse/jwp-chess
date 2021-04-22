package chess.service.spring;

import chess.domain.room.Room;
import chess.repository.spring.RoomDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomDAO roomDAO;

    public RoomService(RoomDAO roomDAO) {
        this.roomDAO = roomDAO;
    }

    public List<Room> findAllRooms() {
        return roomDAO.findAllRooms();
    }

    public void addRoom(String name) {
        roomDAO.insertRoom(name);
    }

    public Room findLastAddedRoom() {
        return roomDAO.findLastAddedRoom()
                .orElseThrow(() -> new IllegalStateException("등록된 방이 없습니다."));
    }

    public void deleteRoomById(int id) {
        roomDAO.deleteRoomById(id);
    }
}
