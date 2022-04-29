package chess.dao;

public interface RoomDao {

    Long insertRoom(String title, String password);

    Long updateStateById(Long roomId, String state);
}
