package chess.dao;

import chess.dto.RoomDto;
import chess.dto.RoomRequestDto;

import java.util.List;

public interface RoomDao {
    long makeRoom(final RoomRequestDto roomRequestDto);

    List<RoomDto> findAll();

    long findIdByRoomName(final String name);

    RoomDto findRoomById(final long id);

    void deleteRoom(final long id);
}
