package chess.dao;

import chess.dto.RoomDto;

import java.util.List;

public interface ChessRoomRepository {
    Long add(RoomDto roomDto);

    List<RoomDto> findAllRoom();

    String findRoomNameById(String roomId);

    void delete(String roomId);
}
