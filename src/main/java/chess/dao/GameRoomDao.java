package chess.dao;

import chess.domain.game.room.Room;
import chess.domain.game.room.RoomId;
import chess.domain.piece.PieceColor;

public interface GameRoomDao {

    void createGameRoom(Room room);

    void deleteGameRoom(RoomId roomId);

    void updateTurnToWhite(RoomId roomId);

    void updateTurnToBlack(RoomId roomId);

    PieceColor getCurrentTurn(RoomId roomId);
}
