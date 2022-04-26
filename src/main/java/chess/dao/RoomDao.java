package chess.dao;

import chess.dto.RoomDto;

public interface RoomDao {
    void makeRoom(final RoomDto roomDto);

    long findIdByRoomName(final String name);

    RoomDto findRoomById(final long id);

    void deleteRoom(final long id);
}
