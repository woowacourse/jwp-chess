package chess.model.service.fake;

import chess.model.room.Room;
import chess.repository.RoomRepository;

import java.util.List;

public class FakeRoomRepository implements RoomRepository<Room> {

    private int fakeAutoIncrementId;
    private String fakeTitle;
    private int boardId;

    public FakeRoomRepository(int fakeAutoIncrementId, String fakeTitle, int boardId) {
        this.fakeAutoIncrementId = fakeAutoIncrementId;
        this.fakeTitle = fakeTitle;
        this.boardId = boardId;
    }

    @Override
    public List<Room> findAllWithRunning() {
        return List.of(new Room(fakeAutoIncrementId, fakeTitle, boardId));
    }

    @Override
    public int deleteAll() {
        return 0;
    }

    @Override
    public Room save(Room room) {
        Room savedRoom = new Room(fakeAutoIncrementId, room.getTitle(), room.getBoardId());
        this.fakeAutoIncrementId++;
        return savedRoom;
    }

    @Override
    public Room getById(int roomId) {
        return new Room(fakeAutoIncrementId, fakeTitle, boardId);
    }
}
