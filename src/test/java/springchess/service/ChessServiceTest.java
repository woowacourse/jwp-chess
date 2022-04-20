package springchess.service;

import chess.dto.RoomsDto;
import chess.model.room.Room;
import chess.service.ChessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import springchess.service.fake.*;

import static org.assertj.core.api.Assertions.assertThat;

class ChessServiceTest {

    private final int fakeBoardId = 101;
    private final int fakeRoomId = 11;
    private final int fakeMemberId = 1;
    private final String fakeTitle = "woowa";
    private final String fakeName = "eden";

    private FakeBoardDao fakeBoardDao;
    private FakeSquareDao fakeSquareDao;
    private FakePieceDao fakePieceDao;
    private FakeRoomDao fakeRoomDao;
    private FakeMemberDao fakeMemberDao;
    private chess.service.ChessService chessService;

    @BeforeEach
    void setup() {
        fakeBoardDao = new FakeBoardDao(fakeBoardId);
        fakeSquareDao = new FakeSquareDao();
        fakePieceDao = new FakePieceDao();
        fakeRoomDao = new FakeRoomDao(fakeRoomId, fakeTitle, fakeBoardId);
        fakeMemberDao = new FakeMemberDao(fakeMemberId, fakeName);
        chessService = new ChessService(fakeBoardDao, fakeSquareDao, fakePieceDao, fakeRoomDao, fakeMemberDao);
    }

    @Test
    void init() {
        Room room = chessService.init(fakeTitle, fakeName, "corinne");

        assertThat(room.getTitle()).isEqualTo(fakeTitle);
    }

    @Test
    void getRooms() {
        RoomsDto roomsDto = chessService.getRooms();

        assertThat(roomsDto.getRoomsDto().get(0).getRoomTitle()).isEqualTo(fakeTitle);
    }
}
