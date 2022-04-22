package chess.model.service;

import chess.dto.RoomsDto;
import chess.model.room.Room;
import chess.model.service.fake.*;
import chess.service.ChessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChessServiceTest {

    private final int fakeBoardId = 101;
    private final int fakeRoomId = 11;
    private final int fakeMemberId = 1;
    private final String fakeTitle = "woowa";
    private final String fakeName = "eden";

    private FakeBoardRepository fakeBoardRepository;
    private FakeSquareRepository fakeSquareRepository;
    private FakePieceRepository fakePieceRepository;
    private FakeRoomRepository fakeRoomRepository;
    private FakeMemberRepository fakeMemberRepository;
    private ChessService chessService;

    @BeforeEach
    void setup() {
        fakeBoardRepository = new FakeBoardRepository(fakeBoardId);
        fakeSquareRepository = new FakeSquareRepository();
        fakePieceRepository = new FakePieceRepository();
        fakeRoomRepository = new FakeRoomRepository(fakeRoomId, fakeTitle, fakeBoardId);
        fakeMemberRepository = new FakeMemberRepository(fakeMemberId, fakeName);
        chessService = new ChessService(fakeBoardRepository, fakeSquareRepository, fakePieceRepository, fakeRoomRepository, fakeMemberRepository);
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
