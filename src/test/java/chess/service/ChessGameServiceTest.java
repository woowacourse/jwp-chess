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
    private final String roomName = "first";

    @BeforeEach
    void setUp() {
        roomDao.saveNewRoom(roomName, "1234");
    }

    @Test
    @DisplayName("게임을 시작할 수 있다.")
    void start() {
        //given
        chessGameService.start(roomName);
        //when
        final Map<Position, Piece> pieces = chessGameService.getPieces(roomName);
        //then
        assertThat(pieces).hasSize(32);
    }

    @Test
    @DisplayName("게임이 시작되지 않았는데 move를 하려고 하면 예외를 발생시킨다.")
    void moveException() {
        //given
        final String anySourcePosition = "a2";
        final String anyTargetPosition = "a4";
        //when then
        assertThatThrownBy(() -> chessGameService.move(roomName, anySourcePosition, anyTargetPosition))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("진행중인 게임이 없습니다.");
    }

    @Test
    @DisplayName("기물을 이동시킬 수 있다.")
    void move() {
        //given
        chessGameService.start(roomName);
        final String sourcePosition = "a2";
        final String targetPosition = "a4";
        chessGameService.move(roomName, sourcePosition, targetPosition);
        //when
        final Map<Position, Piece> pieces = chessGameService.getPieces(roomName);
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
        chessGameService.start(roomName);
        chessGameService.move(roomName, "b2", "b4");
        chessGameService.move(roomName, "a7", "a5");
        chessGameService.move(roomName, "b4", "a5");

        //when
        final Map<Position, Piece> pieces = chessGameService.getPieces(roomName);
        final int blackPieceCount = (int) pieces.values()
                .stream()
                .filter(piece -> piece.isTeamOf(BLACK))
                .count();
        //then
        assertThat(blackPieceCount).isEqualTo(15);
    }

    @Test
    @DisplayName("게임을 종료한다.")
    void end() {
        //given
        chessGameService.start(roomName);
        chessGameService.end(roomName);
        //when
        final Map<Position, Piece> pieces = chessGameService.getPieces(roomName);
        //then
        assertThat(pieces).isEmpty();
    }
}