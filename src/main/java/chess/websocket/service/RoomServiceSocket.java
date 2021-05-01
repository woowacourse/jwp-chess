package chess.websocket.service;

import chess.dao.room.RoomDao;
import chess.domain.TeamColor;
import chess.domain.room.Room;
import chess.domain.room.User;
import chess.service.RoomService;
import chess.service.RoomServiceNormal;
import chess.websocket.ResponseForm;
import chess.websocket.ResponseForm.Form;
import chess.websocket.domain.Lobby;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("socket")
public class RoomServiceSocket implements RoomService {

    private final RoomService roomService;
    private final Lobby lobby;
    private final RoomDao roomDao;
    @Autowired
    private ObjectMapper objectMapper;

    public RoomServiceSocket(RoomDao roomDao, Lobby lobby) {
        this.roomDao = roomDao;
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
    public void enterRoomAsPlayer(Long roomId, String password, TeamColor teamColor, String name,
        User user) {
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
    public void leaveRoom(User user) {
        roomDao.findRoom(user.roomId()).ifPresent(room -> {
            if (user.isPlayer()) {
                try {
                    final String response = objectMapper
                        .writeValueAsString(new ResponseForm<>(Form.REMOVE_ROOM, user.name()));
                    room.users().stream().filter(roomUser -> !roomUser.equals(user))
                        .forEach(roomUser -> roomUser.sendData(response));
                    removeRoom(room.id());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            } else {
                room.leaveRoom(user);
            }
        });
    }

    @Override
    public Room findRoom(Long roomId) {
        return roomService.findRoom(roomId);
    }
}
