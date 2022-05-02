package chess.model.service.fake;

import chess.model.room.Room;
import chess.model.status.Status;
import chess.repository.RoomRepository;

import java.util.List;

public class FakeRoomRepository implements RoomRepository<Room> {

    private int fakeAutoIncrementId;
    private String fakeTitle;
    private String fakePassword;
    private int boardId;

    public FakeRoomRepository(int fakeAutoIncrementId, String fakeTitle, String fakePassword, int boardId) {
        this.fakeAutoIncrementId = fakeAutoIncrementId;
        this.fakeTitle = fakeTitle;
        this.fakePassword = fakePassword;
        this.boardId = boardId;
    }

    @Override
    public List<Room> findAll() {
        return List.of(new Room(fakeAutoIncrementId, fakeTitle, fakePassword, boardId));
    }

    @Override
    public List<Room> findAllByBoardStatus(Status status) {
        return List.of(new Room(fakeAutoIncrementId, fakeTitle, fakePassword, boardId));
    }

    @Override
    public Room save(Room room) {
        Room savedRoom = new Room(fakeAutoIncrementId, room.getTitle(), room.getPassword(), room.getBoardId());
        this.fakeAutoIncrementId++;
        return savedRoom;
    }

    @Override
    public Room getById(int roomId) {
        return new Room(fakeAutoIncrementId, fakeTitle, fakePassword, boardId);
    }

    @Override
    public void deleteById(int id) {
    }
}
