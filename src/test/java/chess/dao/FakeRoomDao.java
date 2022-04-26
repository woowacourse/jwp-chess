package chess.dao;

import java.util.HashMap;
import java.util.Map;

public class FakeRoomDao implements RoomDao {

    private static final String DEFAULT_GAME_STATE = "playing";
    private static final String FIRST_TURN = "WHITE";

    private final Map<String, Room> rooms = new HashMap<>();
    private int roomIndex = 0;

    @Override
    public void saveNewRoom(final String roomName, final String password) {
        final Room room = new Room(roomIndex++, password, DEFAULT_GAME_STATE, FIRST_TURN);
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

    public void updateGameState(final String roomName, final String state) {
        final Room room = rooms.get(roomName);
        final Room updateRoom = new Room(room.index, room.password, state, room.turn);
        rooms.put(roomName, updateRoom);
    }

    class Room {

        private final int index;
        private final String password;
        private final String gameState;
        private final String turn;

        public Room(final int index, final String password, final String gameState, final String turn) {
            this.index = index;
            this.password = password;
            this.gameState = gameState;
            this.turn = turn;
        }
    }
}
