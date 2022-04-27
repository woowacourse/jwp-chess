package chess.model.service.fake;

import chess.dto.RoomContentDto;
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
    public List<RoomContentDto> findAll() {
        return List.of(new RoomContentDto(fakeAutoIncrementId, fakeTitle, boardId, "running"));
    }

    @Override
    public Room save(Room room, String password) {
        Room savedRoom = new Room(fakeAutoIncrementId, room.getTitle(), room.getBoardId());
        this.fakeAutoIncrementId++;
        return savedRoom;
    }

    @Override
    public Room getById(int roomId) {
        return new Room(fakeAutoIncrementId, fakeTitle, boardId);
    }

    @Override
    public String getPasswordById(int roomId) {
        return null;
    }
}
