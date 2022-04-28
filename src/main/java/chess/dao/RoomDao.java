package chess.dao;

import chess.dto.RoomDto;

import java.util.List;

public interface RoomDao {

    void createRoom(String roomName, String password);

    void deleteRoom(int roomId, String password);

    List<RoomDto> findAllRoom();
}
