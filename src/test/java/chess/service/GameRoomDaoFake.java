package chess.service;

import chess.dao.GameRoomDao;
import chess.domain.game.room.Room;
import chess.domain.game.room.RoomId;
import chess.domain.piece.PieceColor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameRoomDaoFake implements GameRoomDao {
    private final Map<Room, PieceColor> fakeGame = new HashMap<>();

    @Override
    public void createGameRoom(Room room) {
        fakeGame.put(room, PieceColor.WHITE);
    }

    @Override
    public List<Room> getRooms() {
        return new ArrayList<>(fakeGame.keySet());
    }

    @Override
    public void deleteGameRoom(RoomId roomId) {
        fakeGame.remove(getRoomByRoomId(roomId));
    }

    @Override
    public void updateTurnToWhite(RoomId roomId) {
        fakeGame.put(getRoomByRoomId(roomId), PieceColor.WHITE);
    }

    @Override
    public void updateTurnToBlack(RoomId roomId) {
        fakeGame.put(getRoomByRoomId(roomId), PieceColor.BLACK);
    }

    @Override
    public PieceColor getCurrentTurn(RoomId roomId) {
        return fakeGame.get(getRoomByRoomId(roomId));
    }

    private Room getRoomByRoomId(RoomId roomId) {
        for (Room room : fakeGame.keySet()) {
            if (room.getId().equals(roomId)) {
                return room;
            }
        }

        throw new IllegalArgumentException("존재하지 않는 방 ID 입니다.");
    }

    @Override
    public String toString() {
        return "GameDaoFake{" +
                "fakeGame=" + fakeGame +
                '}';
    }
}
