package chess.dao;

public interface RoomDao {

    void saveNewRoom(final String roomName, final String password);

    boolean hasDuplicatedName(final String roomName);

    String getPasswordByName(final String roomName);

    String getGameStateByName(final String roomName);

    void saveGameState(final String roomName, final String state);

    void deleteRoomByName(final String roomName);

    void saveTurn(final String roomName, final String turn);

    String getTurn(final String roomName);

    int getRoomId(final String roomName);
}
