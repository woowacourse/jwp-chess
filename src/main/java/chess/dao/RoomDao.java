package chess.dao;

public interface RoomDao {

    void saveNewRoom(final String roomName, final String password);

    boolean hasDuplicatedName(final String roomName);

    String getPasswordByName(final String roomName);

    String getGameStateByName(final String roomName);

    void deleteRoomByName(final String roomName);
}
