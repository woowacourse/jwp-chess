package chess.service;

import chess.dao.room.RoomDao;
import chess.domain.TeamColor;
import chess.domain.room.Room;
import chess.domain.room.RoomInformation;
import chess.domain.room.User;
import chess.exception.PasswordIncorrectException;
import chess.exception.RoomNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("http")
public class RoomServiceNormal implements RoomService {

    private final RoomDao roomDao;
    @Autowired
    private ObjectMapper objectMapper;

    public RoomServiceNormal(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    @Override
    public Long createRoom(String title, boolean locked, String password, String nickname,
        User user) {
        RoomInformation roomInformation = new RoomInformation(title, locked, password);
        Room createdRoom = roomDao.newRoom(roomInformation);
        createdRoom.enterAsPlayer(user, TeamColor.WHITE, nickname);
        return createdRoom.id();
    }

    @Override
    public List<Room> rooms() {
        return roomDao.rooms();
    }

    @Override
    public void enterRoomAsPlayer(Long roomId, String password, TeamColor teamColor,
        String nickname, User user) {
        Room room = roomDao.findRoom(roomId).orElseThrow(RoomNotFoundException::new);
        if (room.isLocked() && room.passwordIncorrect(password)) {
            throw new PasswordIncorrectException();
        }
        room.enterAsPlayer(user, teamColor, nickname);
    }

    @Override
    public Room findRoom(Long roomId) {
        return roomDao.findRoom(roomId).orElseThrow(RoomNotFoundException::new);
    }

    @Override
    public void enterRoomAsParticipant(Long roomId, String password, String nickname, User user) {
        Room room = roomDao.findRoom(roomId).orElseThrow(RoomNotFoundException::new);
        if (room.isLocked() && room.passwordIncorrect(password)) {
            throw new PasswordIncorrectException();
        }
        room.enterAsParticipant(roomId, user, nickname);
    }

    @Override
    public void removeRoom(Long id) {
        roomDao.removeRoom(id);
    }

    @Override
    public void leaveRoom(User user) {
        final Room findRoom = roomDao.findRoom(user.roomId())
            .orElseThrow(RoomNotFoundException::new);
        findRoom.leaveRoom(user);
    }
}
