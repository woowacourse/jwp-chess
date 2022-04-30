package chess.dao;

import chess.dto.RoomDto;

import java.util.List;

public interface RoomDao {

    void createRoom(final String roomName, final String password);

    void deleteRoom(final int roomNumber, final String password);

    List<RoomDto> findAllRoom();

    boolean checkRoom(final int roomNumber, final String password);
}
