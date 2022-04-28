package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import chess.dao.RoomDao;
import chess.dto.RoomDto;
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
        Optional<Room> room = findByName(name);
        if (room.isPresent() && room.get().getPassword().equals(password)) {
            return room;
        }
        return Optional.empty();
    }

    @Override
    public Optional<Room> findById(Long roomId) {
        return Optional.ofNullable(rooms.get((int)(roomId - 1)));
    }

    @Override
    public void update(Long id, String turn) {
        Room room = rooms.get((int)(id - 1));
        rooms.set((int)(id - 1), new Room(id, room.getPassword(), turn, room.getName()));
    }

    @Override
    public List<RoomDto> findAll() {
        return rooms.stream()
            .map(room -> new RoomDto(room.getId(), room.getTurn(), room.getName()))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Optional<Room> findByIdAndPassword(Long id, String password) {
        Room room = rooms.get((int) (id - 1));
        if (room.getPassword().equals(password)) {
            return Optional.of(room);
        }
        return Optional.empty();
    }

    @Override
    public void delete(Long roomId) {
        rooms.remove((int) (roomId - 1));
    }
}
