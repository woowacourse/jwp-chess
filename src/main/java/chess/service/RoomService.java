package chess.service;

import chess.domain.TeamColor;
import chess.domain.room.Room;
import chess.domain.room.User;
import java.util.List;

public interface RoomService {

    Long createRoom(String title, boolean locked, String password, User user);

    List<Room> rooms();

    void enterRoomAsPlayer(Long roomId, String password, TeamColor teamColor, User user);
}
