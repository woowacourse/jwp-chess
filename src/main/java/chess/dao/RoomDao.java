package chess.dao;

public interface RoomDao {

    void saveNewRoom(final String roomName, final String password);

    boolean hasDuplicatedName(final String roomName);
}
