package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import chess.dao.RoomDao;
import chess.entity.Room;

public class FakeRoomDao implements RoomDao {

    private final List<Room> rooms = new ArrayList<>();
    private Long id;

    public FakeRoomDao() {
        this.id = 1L;
    }

    @Override
    public void save(Room room) {
        rooms.add(new Room(id++, room.getPassword(), room.getTurn(), room.getName()));
    }

    @Override
    public Optional<Room> findByName(String name) {

        return rooms.stream()
            .filter(room -> room.getName().equals(name))
            .findAny();
    }

    @Override
    public Optional<Room> findByNameAndPassword(String name, String password) {
        Room room = findByName(name).get();
        if (room.getPassword().equals(password)) {
            return Optional.of(room);
        }
        return Optional.empty();
    }

    @Override
    public void update(long id, String turn) {
        Room room = rooms.get((int)(id - 1));
        rooms.set((int)(id - 1), new Room(id, room.getPassword(), turn, room.getName()));
    }
}
