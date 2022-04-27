package chess.service;

import chess.dao.GameRoomDao;
import chess.domain.game.room.Room;
import chess.domain.game.room.RoomId;
import chess.domain.piece.PieceColor;
import java.util.HashMap;
import java.util.Map;

public class GameRoomDaoFake implements GameRoomDao {
    private final Map<RoomId, PieceColor> fakeGame = new HashMap<>();

    @Override
    public void createGameRoom(Room room) {
        fakeGame.put(room.getId(), PieceColor.WHITE);
    }

    @Override
    public void deleteGameRoom(RoomId roomId) {
        fakeGame.remove(roomId);
    }

    @Override
    public void updateTurnToWhite(RoomId roomId) {
        fakeGame.put(roomId, PieceColor.WHITE);
    }

    @Override
    public void updateTurnToBlack(RoomId roomId) {
        fakeGame.put(roomId, PieceColor.BLACK);
    }

    @Override
    public PieceColor getCurrentTurn(RoomId roomId) {
        return fakeGame.get(roomId);
    }

    @Override
    public String toString() {
        return "GameDaoFake{" +
                "fakeGame=" + fakeGame +
                '}';
    }
}
