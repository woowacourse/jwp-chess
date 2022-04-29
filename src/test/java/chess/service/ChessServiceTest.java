package chess.service;

import static chess.domain.board.position.File.A;
import static chess.domain.board.position.Rank.FOUR;
import static chess.domain.board.position.Rank.TWO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dao.FakeBoardPieceDao;
import chess.dao.FakeGameDao;
import chess.domain.board.ChessBoard;
import chess.domain.board.position.Position;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.dto.request.web.SaveRequest;
import chess.dto.response.web.GameResponse;
import chess.repository.SessionToChessRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

class ChessServiceTest {

    private ChessService service = new ChessService(new FakeGameDao(), new FakeBoardPieceDao(), new SessionToChessRepository());

    @BeforeEach
    void setUp() {
        service = new ChessService(new FakeGameDao(), new FakeBoardPieceDao(), new SessionToChessRepository());
    }

    @DisplayName("session 에 해당하는 체스보드를 생성하고 조회가 되는 지 테스트")
    @Test
    void create_board_by_session() {
        MockHttpSession firstSession = new MockHttpSession();
        MockHttpSession secondSession = new MockHttpSession();

        service.initAndGetChessBoard(firstSession);

        assertThat(service.getChessBoard(firstSession)).isNotNull();
        assertThat(service.getChessBoard(secondSession)).isNull();
    }

    @DisplayName("체스보드의 기물을 움직일 수 있는지 테스트")
    @Test
    void save_and_move() {
        MockHttpSession session = new MockHttpSession();

        ChessBoard chessBoard = service.initAndGetChessBoard(session);

        Position from = Position.of(A, TWO);
        Position to = Position.of(A, FOUR);
        service.movePiece(session, from, to);

        Piece findPiece = chessBoard.getBoard().get(Position.of(A, FOUR));
        assertThat(findPiece).isInstanceOf(Pawn.class);
    }

    @DisplayName("생성 존재 여부 반환")
    @Test
    void create_and_lastFindChessBoard() {
        boolean beforeSave = service.isExistGame();
        service.saveGame(new SaveRequest("Black Team", new HashMap<>(), LocalDateTime.now()));
        boolean afterSave = service.isExistGame();

        assertAll(
                () -> assertThat(beforeSave).isEqualTo(false),
                () -> assertThat(afterSave).isEqualTo(true)
        );
    }

    @DisplayName("가장 마지막 데이터를 조회")
    @Test
    void find_lastGame() {
        service.saveGame(new SaveRequest("White Team", Map.of("a2", "whitePawn"), LocalDateTime.of(2022, 04, 24, 00,00)));
        service.saveGame(new SaveRequest("White Team", Map.of("b2", "whitePawn"), LocalDateTime.of(2022, 04, 25, 00,00)));
        service.saveGame(new SaveRequest("Black Team", Map.of("a7", "blackPawn"), LocalDateTime.of(2022, 04, 26, 00,00)));

        GameResponse gameResponse = service.loadLastGame();
        assertThat(gameResponse.getTeamName()).isEqualTo("Black Team");
    }
}