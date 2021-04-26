package chess.service.spring;

import chess.domain.room.Room;
import chess.repository.spring.RoomDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final UserService userService;
    private final RoomDAO roomDAO;

    public RoomService(UserService userService, RoomDAO roomDAO) {
        this.userService = userService;
        this.roomDAO = roomDAO;
    }

    public List<Room> findAllRooms() {
        return roomDAO.findAllRooms();
    }

    public void addRoom(String name) { //삭제할 메서드.
        roomDAO.insertRoom(name);
    }

    public void deleteRoomById(int id) {
        roomDAO.deleteRoomById(id);
    }

    public int addRoom(String name, String password) {
        int roomId = roomDAO.insertRoom(name);
        userService.addUserIntoRoom(roomId, password);
        return roomId;
    }
}
