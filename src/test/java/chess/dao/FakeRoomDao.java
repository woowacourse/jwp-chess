package chess.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chess.domain.board.BoardDto;
import chess.domain.room.Room;
import chess.repository.RoomRepository;

public class FakeRoomDao implements RoomRepository {

    private final Map<Long, Room> fakeRoomTable;

    public FakeRoomDao() {
        this.fakeRoomTable = new HashMap<>();
    }

    @Override
    public List<Room> findAll() {
        return new ArrayList<>(fakeRoomTable.values());
    }

    @Override
    public long insert(Room room, BoardDto boardDto) {
        long roomId = fakeRoomTable.size() + 1L;
        fakeRoomTable.put(roomId, room);
        return roomId;
    }
}
