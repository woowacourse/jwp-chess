package chess.dao;

import chess.entity.RoomEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FakeRoomDao implements RoomDao {

    private final Map<Integer, Room> rooms = new HashMap<>();
    private int roomIndex = 1;

    @Override
    public void saveNewRoom(final String roomName, final String password) {
        final Room room = new Room(roomName, password, "ready", "WHITE");
        rooms.put(roomIndex++, room);
    }

    @Override
    public boolean hasDuplicatedName(final String roomName) {
        return rooms.values()
                .stream()
                .anyMatch(room -> room.name.equals(roomName));
    }

    @Override
    public void deleteRoomByName(final int roomId) {
        rooms.remove(roomId);
    }

    @Override
    public void saveGameState(final int roomId, final String state) {
        final Room room = rooms.get(roomId);
        room.gameState = state;
    }

    @Override
    public void saveTurn(final int roomId, final String turn) {
        final Room room = rooms.get(roomId);
        room.turn = turn;
    }

    @Override
    public RoomEntity findByRoomId(final int roomId) {
        final Room room = rooms.get(roomId);
        return new RoomEntity(roomId, room.name, room.password, room.gameState, room.turn);
    }

    @Override
    public List<RoomEntity> findAllRooms() {
        final List<RoomEntity> rooms = new ArrayList<>();
        for (Entry<Integer, Room> room : this.rooms.entrySet()) {
            final int roomId = room.getKey();
            final Room value = room.getValue();
            rooms.add(new RoomEntity(roomId, value.name, value.password, value.gameState, value.turn));
        }
        return rooms;
    }

    class Room {

        private final String name;
        private final String password;
        private String gameState;
        private String turn;

        public Room(final String name, final String password, final String gameState, final String turn) {
            this.name = name;
            this.password = password;
            this.gameState = gameState;
            this.turn = turn;
        }
    }
}
