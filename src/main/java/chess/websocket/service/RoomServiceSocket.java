package chess.websocket.service;

import chess.dao.room.RoomDao;
import chess.domain.TeamColor;
import chess.domain.room.Room;
import chess.domain.room.User;
import chess.service.RoomService;
import chess.service.RoomServiceNormal;
import chess.websocket.domain.Lobby;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("socket")
public class RoomServiceSocket implements RoomService {

    private final RoomService roomService;
    private final RoomDao roomDao;
    private final Lobby lobby;
    private final ObjectMapper objectMapper;

    public RoomServiceSocket(RoomDao roomDao, Lobby lobby, ObjectMapper objectMapper) {
        this.roomDao = roomDao;
        this.roomService = new RoomServiceNormal(roomDao);
        this.lobby = lobby;
        this.objectMapper = objectMapper;
    }

    @Override
    public Long createRoom(String title, boolean locked, String password, User user) {
        Long roomId = roomService.createRoom(title, locked, password, user);
        lobby.leave(user);
        return roomId;
    }

    @Override
    public List<Room> rooms() {
        return roomService.rooms();
    }

    @Override
    public void enterRoomAsPlayer(Long roomId, String password, TeamColor teamColor, User user) {
        roomService.enterRoomAsPlayer(roomId, password, teamColor, user);
        lobby.leave(user);
    }

    @Override
    public Optional<Room> findRoom(Long roomId) {
        return roomService.findRoom(roomId);
    }
}
