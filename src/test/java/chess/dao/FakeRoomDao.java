package chess.dao;

import chess.entity.RoomEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeRoomDao implements RoomDao {

    private final List<RoomEntity> rooms = new ArrayList<>();
    private Long id;

    public FakeRoomDao() {
        this.id = 1L;
    }

    @Override
    public long save(RoomEntity room) {
        rooms.add(new RoomEntity(id, room.getTurn(), room.getName(), room.getPassword()));
        return id++;
    }

    @Override
    public Optional<RoomEntity> findById(long id) {
        return rooms.stream()
                .filter(room -> room.getId() == id)
                .findAny();
    }

    @Override
    public List<RoomEntity> findAll() {
        return rooms;
    }

    @Override
    public void updateTurn(long id, String turn) {
        RoomEntity room = rooms.get((int) (id - 1));
        rooms.set((int) (id - 1), new RoomEntity(id, turn, room.getName(), room.getPassword()));
    }

    @Override
    public void deleteRoom(long id) {
        RoomEntity delRoom = rooms.stream()
                .filter(room -> room.getId() == id)
                .findAny().orElseThrow();
        rooms.remove(delRoom);
    }
}
