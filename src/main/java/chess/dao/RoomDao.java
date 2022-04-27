package chess.dao;

public interface RoomDao {

    void saveNewRoom(final String roomName, final String password);

    boolean hasDuplicatedName(final String roomName);

    String getPasswordByName(final int roomId);

    String getGameStateByName(final int roomId);

    void saveGameState(final int roomId, final String state);

    void deleteRoomByName(final int roomId);

    void saveTurn(final int roomId, final String turn);

    String getTurn(final int roomId);
}
