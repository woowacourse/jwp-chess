package chess.dao;

import chess.dto.RoomDto;

import java.util.List;

public interface RoomDao {

    void createRoom(final String roomName, final String password);

    void deleteRoom(final int roomId, final String password);

    List<RoomDto> findAllRoom();
}
