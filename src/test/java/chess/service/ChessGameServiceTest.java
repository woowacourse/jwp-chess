package chess.service;

import static chess.domain.piece.Team.BLACK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.dao.FakePieceDao;
import chess.dao.FakeRoomDao;
import chess.dao.RoomDao;
import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
class ChessGameServiceTest {

    private final RoomDao roomDao = new FakeRoomDao();

    private final ChessGameService chessGameService =
            new ChessGameService(new FakePieceDao(), roomDao);
    private static final int TEST_ROOM_ID = 1;

    @BeforeEach
    void setUp() {
        roomDao.saveNewRoom("first", "1234");
    }

    @Test
    @DisplayName("게임을 시작할 수 있다.")
    void start() {
        //given
        chessGameService.start(TEST_ROOM_ID);
        //when
        final Map<Position, Piece> pieces = chessGameService.getPieces(TEST_ROOM_ID);
        //then
        assertThat(pieces).hasSize(32);
    }

    @Test
    @DisplayName("게임이 진행중인데, 게임 시작을 하려고 하면 예외를 발생시킨다.")
    void startException() {
        //given
        chessGameService.start(TEST_ROOM_ID);
        //when then
        assertThatThrownBy(() -> chessGameService.start(TEST_ROOM_ID))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 진행중인 게임이 있습니다.");
    }

    @Test
    @DisplayName("게임이 시작되지 않았는데 move를 하려고 하면 예외를 발생시킨다.")
    void moveException() {
        //given
        final String anySourcePosition = "a2";
        final String anyTargetPosition = "a4";
        //when then
        assertThatThrownBy(() -> chessGameService.move(TEST_ROOM_ID, anySourcePosition, anyTargetPosition))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("진행중인 게임이 없습니다.");
    }

    @Test
    @DisplayName("기물을 이동시킬 수 있다.")
    void move() {
        //given
        chessGameService.start(TEST_ROOM_ID);
        final String sourcePosition = "a2";
        final String targetPosition = "a4";
        chessGameService.move(TEST_ROOM_ID, sourcePosition, targetPosition);
        //when
        final Map<Position, Piece> pieces = chessGameService.getPieces(TEST_ROOM_ID);
        final Piece pieceInSourcePosition = pieces.get(Position.from(sourcePosition));
        final Piece pieceInTargetPosition = pieces.get(Position.from(targetPosition));
        //then
        assertThat(pieceInSourcePosition).isNull();
        assertThat(pieceInTargetPosition).isNotNull();
    }

    @Test
    @DisplayName("상대 기물을 공격하면서 움직일 수 있다.")
    void moveAttack() {
        //given
        chessGameService.start(TEST_ROOM_ID);
        chessGameService.move(TEST_ROOM_ID, "b2", "b4");
        chessGameService.move(TEST_ROOM_ID, "a7", "a5");
        chessGameService.move(TEST_ROOM_ID, "b4", "a5");

        //when
        final Map<Position, Piece> pieces = chessGameService.getPieces(TEST_ROOM_ID);
        final int blackPieceCount = (int) pieces.values()
                .stream()
                .filter(piece -> piece.isTeamOf(BLACK))
                .count();
        //then
        assertThat(blackPieceCount).isEqualTo(15);
    }

    @Test
    @DisplayName("한 왕이 죽었는데, 기물을 이동시키려하면 예외를 발생시킨다.")
    void moveExceptionByOneKing() {
        //given
        chessGameService.start(TEST_ROOM_ID);
        chessGameService.move(TEST_ROOM_ID, "e2", "e4");
        chessGameService.move(TEST_ROOM_ID, "d7", "d6");
        chessGameService.move(TEST_ROOM_ID, "d1", "g4");
        chessGameService.move(TEST_ROOM_ID, "e8", "d7");
        chessGameService.move(TEST_ROOM_ID, "g4", "e7");
        //when then
        assertThatThrownBy(() -> chessGameService.move(TEST_ROOM_ID, "a7", "a5"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("King이 죽어 게임이 종료되었습니다.");
    }

    @Test
    @DisplayName("게임을 종료한다.")
    void end() {
        //given
        chessGameService.start(TEST_ROOM_ID);
        chessGameService.end(TEST_ROOM_ID);
        //when
        final Map<Position, Piece> pieces = chessGameService.getPieces(TEST_ROOM_ID);
        //then
        assertThat(pieces).isEmpty();
        assertThatThrownBy(() -> chessGameService.getScore(TEST_ROOM_ID))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("진행중인 게임이 없습니다.");
    }
}