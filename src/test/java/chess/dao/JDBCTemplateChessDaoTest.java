package chess.dao;

import static chess.domain.Fixture.E2;
import static chess.domain.Fixture.E4;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.dto.GameCreateRequest;
import chess.dto.GameDto;
import chess.dto.MoveRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JDBCTemplateChessDaoTest {

    @Autowired
    private ChessDao chessDao;

    private List<Integer> gameIds = new ArrayList<>();

    @BeforeEach
    void setup() {
        for (int i = 1; i <= 3; i++) {
            final GameCreateRequest gameRequest = new GameCreateRequest(
                    "test room" + i, "password", "white", "black");
            final int gameId = chessDao.create(gameRequest);
            gameIds.add(gameId);
        }
    }

    @AfterEach
    void clear() {
        final List<GameDto> all = chessDao.findAll();
        for (GameDto gameDto : all) {
            chessDao.deleteById(gameDto.getId());
        }
        gameIds = new ArrayList<>();
    }

    @Test
    @DisplayName("게임 아이디(식별자)로 보드를 조회할 수 있다.")
    void findBoardByGameId() {
        for (Integer gameId : gameIds) {
            assertThatCode(() -> chessDao.findBoardByGameId(gameId))
                    .doesNotThrowAnyException();
        }
    }

    @Test
    @DisplayName("방제목, 비밀번호, 흰색닉네임, 검정색닉네임으로 게임을 생성할 수 있다.")
    void create() {
        final GameCreateRequest gameCreateRequest = new GameCreateRequest(
                "new test room", "password", "whiteNickName", "blackNickName");
        final List<GameDto> before = chessDao.findAll();
        final int gameId = chessDao.create(gameCreateRequest);
        final List<GameDto> after = chessDao.findAll();

        final int maxId = after.stream()
                .mapToInt(GameDto::getId)
                .max()
                .orElse(0);

        assertAll(
                () -> assertThat(gameId).isEqualTo(maxId),
                () -> assertThat(before.size() + 1).isEqualTo(after.size())
        );
    }

    @Test
    @DisplayName("게임 아이디로 턴을 변경할 수 있다")
    void changeTurnByGameId() {
        int targetId = gameIds.get(0);

        chessDao.changeTurnByGameId(targetId);
        final Board blackTurnBoard = chessDao.findBoardByGameId(targetId);

        chessDao.changeTurnByGameId(targetId);
        final Board whiteTurnBoard = chessDao.findBoardByGameId(targetId);

        assertAll(
                () -> assertThatThrownBy(() -> blackTurnBoard.move(E2, E4))
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessageContaining("BLACK이 둘 차례입니다"),
                () -> assertThatCode(() -> whiteTurnBoard.move(E2, E4))
                        .doesNotThrowAnyException()
        );
    }

    @Test
    @DisplayName("게임 아이디로 삭제할 수 있다")
    void deleteById() {
        final List<GameDto> before = chessDao.findAll();
        chessDao.deleteById(gameIds.get(0));
        final List<GameDto> after = chessDao.findAll();
        assertThat(after.size()).isEqualTo(before.size() - 1);
    }

    @Test
    @DisplayName("삭제되지 않은 전체 게임 목록을 조회할 수 있다")
    void findAll() {
        final List<GameDto> fromDB = chessDao.findAll();
        assertThat(fromDB.size()).isEqualTo(gameIds.size());
    }

    @Test
    @DisplayName("게임 아이디로 게임 정보를 조회할 수 있다")
    void findById() {
        final int targetId = gameIds.get(0);
        assertThat(chessDao.findById(targetId)).isNotNull();
    }

    @Test
    @DisplayName("게임 아이디로 암호화된 비밀번호를 조회할 수 있다")
    void findPasswordById() {
        final int targetId = gameIds.get(0);
        final String passwordById = chessDao.findPasswordById(targetId);
        assertAll(
                () -> assertThat(passwordById).isNotEqualTo("password"),
                () -> assertThat(passwordById).contains("$2a$10")
        );
    }

    @Test
    @DisplayName("기물 이동 결과를 저장할 수 있다")
    void updateBoardByMove() {
        final int targetId = gameIds.get(0);

        final Board before = chessDao.findBoardByGameId(targetId);
        final Map<Position, Piece> beforeBoard = before.getBoard();

        chessDao.updateBoardByMove(new MoveRequest("pw", "E2", "E4", targetId));

        final Board after = chessDao.findBoardByGameId(targetId);
        final Map<Position, Piece> afterBoard = after.getBoard();

        assertAll(
                () -> assertThat(beforeBoard.get(E2)).isEqualTo(PieceFactory.getInstance("pw")),
                () -> assertThat(beforeBoard.get(E4)).isNull(),
                () -> assertThat(afterBoard.get(E2)).isNull(),
                () -> assertThat(afterBoard.get(E4)).isEqualTo(PieceFactory.getInstance("pw"))
        );
    }
}
