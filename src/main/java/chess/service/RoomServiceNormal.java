package chess.service;

import chess.dao.room.RoomDao;
import chess.domain.TeamColor;
import chess.domain.room.Room;
import chess.domain.room.RoomInformation;
import chess.domain.room.User;
import chess.exception.PasswordIncorrectException;
import chess.exception.RoomNotFoundException;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("http")
public class RoomServiceNormal implements RoomService {

    private final RoomDao roomDao;

    public RoomServiceNormal(RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    @Override
    public Long createRoom(String title, boolean locked, String password, User user) {
        RoomInformation roomInformation = new RoomInformation(title, locked, password);
        Room createdRoom = roomDao.newRoom(roomInformation);
        createdRoom.enterAsPlayer(user, TeamColor.WHITE);
        return createdRoom.id();
    }

    @Override
    public List<Room> rooms() {
        return roomDao.rooms();
    }

    @Override
    public void enterRoomAsPlayer(Long roomId, String password, TeamColor teamColor, User user) {
        Room room = roomDao.findRoom(roomId).orElseThrow(RoomNotFoundException::new);
        if (room.isLocked() && room.passwordIncorrect(password)) {
            throw new PasswordIncorrectException();
        }
        room.enterAsPlayer(user, teamColor);
    }
//
//    @Override
//    public Optional<Room> roomById(Long gameId) {
//        return roomDao.findRoom(gameId);
//    }
}
