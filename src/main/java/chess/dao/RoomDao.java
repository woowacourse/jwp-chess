package chess.dao;

import chess.entity.Room;
import java.util.List;

public interface RoomDao {

    List<Room> findAllRoom();

    Long insertRoom(String title, String password);

    Long updateStateById(Long roomId, String state);

    Room findRoomById(Long roomId);

    Long deleteRoom(Long roomId);
}
