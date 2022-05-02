package chess.service;

import chess.dao.RoomDao;
import chess.domain.game.room.Room;
import chess.domain.game.room.RoomId;
import chess.domain.game.room.RoomPassword;
import chess.domain.piece.PieceColor;
import chess.exception.NotFoundRoomException;

import java.util.*;

public class RoomDaoFake implements RoomDao {
    private final Map<Room, PieceColor> fakeRoom = new HashMap<>();

    @Override
    public void createRoom(Room room) {
        fakeRoom.put(room, PieceColor.WHITE);
    }

    @Override
    public List<Room> getAllRooms() {
        return new ArrayList<>(fakeRoom.keySet());
    }

    @Override
    public void deleteRoom(RoomId roomId, RoomPassword roomPassword) {
        validateRoomExisting(roomId);
        if (getRoomByRoomId(roomId).getPassword().equals(roomPassword)) {
            fakeRoom.remove(getRoomByRoomId(roomId));
        }
    }

    @Override
    public void updateTurnToWhite(RoomId roomId) {
        validateRoomExisting(roomId);
        fakeRoom.put(getRoomByRoomId(roomId), PieceColor.WHITE);
    }

    @Override
    public void updateTurnToBlack(RoomId roomId) {
        validateRoomExisting(roomId);
        fakeRoom.put(getRoomByRoomId(roomId), PieceColor.BLACK);
    }

    @Override
    public PieceColor getCurrentTurn(RoomId roomId) {
        validateRoomExisting(roomId);
        return fakeRoom.get(getRoomByRoomId(roomId));
    }

    @Override
    public void validateRoomExisting(RoomId roomId) {
        if (Objects.isNull(getRoomByRoomId(roomId))) {
            throw new NotFoundRoomException();
        }
    }

    private Room getRoomByRoomId(RoomId roomId) {
        for (Room room : fakeRoom.keySet()) {
            if (room.getId().equals(roomId)) {
                return room;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "GameDaoFake{" +
                "fakeGame=" + fakeRoom +
                '}';
    }
}
