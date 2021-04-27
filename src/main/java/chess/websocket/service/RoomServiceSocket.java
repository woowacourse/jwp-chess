package chess.websocket.service;

import chess.dao.room.RoomDao;
import chess.domain.TeamColor;
import chess.domain.room.Room;
import chess.domain.room.User;
import chess.service.RoomService;
import chess.service.RoomServiceNormal;
import chess.websocket.domain.Lobby;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("socket")
public class RoomServiceSocket implements RoomService {

    private final RoomService roomService;
    private final Lobby lobby;

    public RoomServiceSocket(RoomDao roomDao, Lobby lobby) {
        this.roomService = new RoomServiceNormal(roomDao);
        this.lobby = lobby;
    }

    @Override
    public Long createRoom(String title, boolean locked, String password, String name, User user) {
        Long roomId = roomService.createRoom(title, locked, password, name, user);
        lobby.leave(user);
        return roomId;
    }

    @Override
    public List<Room> rooms() {
        return roomService.rooms();
    }

    @Override
    public void enterRoomAsPlayer(Long roomId, String password, TeamColor teamColor, String name, User user) {
        roomService.enterRoomAsPlayer(roomId, password, teamColor, name, user);
        lobby.leave(user);
    }

    @Override
    public void enterRoomAsParticipant(Long roomId, String password, String nickname, User user) {
        roomService.enterRoomAsParticipant(roomId, password, nickname, user);
        lobby.leave(user);
    }

    @Override
    public void removeRoom(Long id) {
        roomService.removeRoom(id);
    }

    @Override
    public Optional<Room> findRoom(Long roomId) {
        return roomService.findRoom(roomId);
    }
}
