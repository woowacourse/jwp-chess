package chess.dao;

import java.util.HashMap;
import java.util.Map;

public class FakeRoomDao implements RoomDao {

    private final Map<String, Room> rooms = new HashMap<>();
    private int roomIndex = 0;

    @Override
    public void saveNewRoom(final String roomName, final String password) {
        final Room room = new Room(roomIndex++, password, "ready", "WHITE");
        rooms.put(roomName, room);
    }

    @Override
    public boolean hasDuplicatedName(final String roomName) {
        return rooms.keySet()
                .stream()
                .anyMatch(name -> name.equals(roomName));
    }

    @Override
    public String getPasswordByName(final String roomName) {
        final Room room = rooms.get(roomName);
        return room.password;
    }

    @Override
    public String getGameStateByName(final String roomName) {
        final Room room = rooms.get(roomName);
        return room.gameState;
    }

    @Override
    public void deleteRoomByName(final String roomName) {
        rooms.remove(roomName);
    }

    @Override
    public void saveGameState(final String roomName, final String state) {
        final Room room = rooms.get(roomName);
        room.gameState = state;
    }

    @Override
    public void saveTurn(final String roomName, final String turn) {
        final Room room = rooms.get(roomName);
        room.turn = turn;
    }

    @Override
    public String getTurn(final String roomName) {
        final Room room = rooms.get(roomName);
        return room.turn;
    }

    @Override
    public int getRoomId(final String roomName) {
        final Room room = rooms.get(roomName);
        return room.index;
    }

    class Room {

        private final int index;
        private final String password;
        private String gameState;
        private String turn;

        public Room(final int index, final String password, final String gameState, final String turn) {
            this.index = index;
            this.password = password;
            this.gameState = gameState;
            this.turn = turn;
        }
    }
}
