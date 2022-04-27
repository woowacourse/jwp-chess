package chess.dao;

import chess.entity.Room;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeRoomDao implements RoomDao {

    private final List<Room> rooms = new ArrayList<>();
    private Long id;

    public FakeRoomDao() {
        this.id = 1L;
    }

    @Override
    public long save(Room room) {
        rooms.add(new Room(id, room.getTurn(), room.getName(), room.getPassword()));
        return id++;
    }

    @Override
    public Optional<Room> findById(long id) {
        return rooms.stream()
                .filter(room -> room.getId() == id)
                .findAny();
    }

    @Override
    public List<Room> findAll() {
        return rooms;
    }

    @Override
    public void updateTurn(long id, String turn) {
        Room room = rooms.get((int) (id - 1));
        rooms.set((int) (id - 1), new Room(id, turn, room.getName(), room.getPassword()));
    }

    @Override
    public void deleteRoom(long id) {
        Room delRoom = rooms.stream()
                .filter(room -> room.getId() == id)
                .findAny().orElseThrow();
        rooms.remove(delRoom);
    }
}
