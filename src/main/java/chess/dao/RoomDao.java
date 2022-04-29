package chess.dao;

import chess.entity.Room;

public interface RoomDao {

    Long insertRoom(String title, String password);

    Long updateStateById(Long roomId, String state);

    Room findRoomById(Long roomId);

    Long deleteRoom(Long roomId);
}
