package chess.dao;

import chess.domain.piece.PieceColor;
import chess.dto.RoomNameDto;

import java.util.List;

public interface RoomDao {
    List<RoomNameDto> findRoomNames();

    String findRoomTurnColor(String roomName);

    void addRoom(String name, PieceColor turnColor);

    void deleteRoom(String roomName);

    void updateTurn(String roomName, PieceColor turnColor);

    boolean existsRoom(String name);
}
