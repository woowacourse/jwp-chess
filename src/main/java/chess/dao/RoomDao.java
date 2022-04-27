package chess.dao;

import chess.dto.RoomDto;

import java.util.List;

public interface RoomDao {
    long makeRoom(final RoomDto roomDto);

    List<RoomDto> findAll();

    long findIdByRoomName(final String name);

    RoomDto findRoomById(final long id);

    void deleteRoom(final long id);
}
