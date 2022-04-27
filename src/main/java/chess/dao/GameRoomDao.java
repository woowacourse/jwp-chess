package chess.dao;

import chess.domain.game.room.Room;
import chess.domain.game.room.RoomId;
import chess.domain.piece.PieceColor;
import java.util.List;

public interface GameRoomDao {

    void createGameRoom(Room room);

    List<Room> getRooms();

    void deleteGameRoom(RoomId roomId);

    void updateTurnToWhite(RoomId roomId);

    void updateTurnToBlack(RoomId roomId);

    PieceColor getCurrentTurn(RoomId roomId);
}
