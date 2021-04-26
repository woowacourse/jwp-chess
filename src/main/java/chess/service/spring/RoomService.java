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

    public List<Room> findAll() {
        return roomDAO.findAll();
    }

    public int addRoom(String name, String password) {
        int roomId = roomDAO.insertRoom(name);
        userService.addUserIntoRoom(roomId, password);
        return roomId;
    }

    public void deleteById(int id) {
        roomDAO.deleteById(id);
    }
}
