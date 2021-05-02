package chess.service;

import chess.domain.TeamColor;
import chess.domain.room.Room;
import chess.domain.room.User;
import java.util.List;

public interface RoomService {

    Long createRoom(String title, boolean locked, String password, String nickname, User user);

    List<Room> rooms();

    void enterRoomAsPlayer(Long roomId, String password, TeamColor teamColor, String nickname,
        User user);

    Room findRoom(Long roomId);

    void enterRoomAsParticipant(Long roomId, String password, String nickname, User user);

    void removeRoom(Long id);

    void leaveRoom(User user);
}
