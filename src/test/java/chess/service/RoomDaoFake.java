package chess.service;

import chess.dao.RoomDao;
import chess.domain.game.room.Room;
import chess.domain.game.room.RoomId;
import chess.domain.game.room.RoomPassword;
import chess.domain.piece.PieceColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomDaoFake implements RoomDao {
    private final Map<Room, PieceColor> fakeGame = new HashMap<>();

    @Override
    public void createRoom(Room room) {
        fakeGame.put(room, PieceColor.WHITE);
    }

    @Override
    public List<Room> getRooms() {
        return new ArrayList<>(fakeGame.keySet());
    }

    @Override
    public void deleteRoom(RoomId roomId, RoomPassword roomPassword) {
        if (getRoomByRoomId(roomId).getPassword().equals(roomPassword)) {
            fakeGame.remove(getRoomByRoomId(roomId));
        }
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
