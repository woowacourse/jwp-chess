package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import chess.dao.fake.FakeBoardDao;
import chess.dao.fake.FakeGameDao;
import chess.dao.fake.FakeGameStatusDao;
import chess.dao.fake.FakeTurnDao;
import chess.domain.board.strategy.BasicBoardStrategy;
import chess.domain.board.strategy.WebBasicBoardStrategy;
import chess.dto.GameStatusDto;
import chess.dto.ScoreDto;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChessGameServiceTest {

    private ChessGameService chessGameService;

    @BeforeEach
    void setUp() {
        chessGameService = new ChessGameService(new FakeBoardDao(new BasicBoardStrategy()), new FakeTurnDao(),
                new FakeGameStatusDao(), new FakeGameDao());
    }

    @Test
    @DisplayName("Board 생성 전략을 전달받아 ChessGame을 시작한다.")
    void startChessGame() {
        GameStatusDto gameStatusDto = chessGameService.startChessGame(new WebBasicBoardStrategy(), 1);

        assertAll(
                () -> assertEquals("PLAYING", gameStatusDto.getGameStatus()),
                () -> assertEquals("white", gameStatusDto.getTurn())
        );
    }

    @Test
    @DisplayName("처음 게임 시작한 후, white, black 점수는 38점이다.")
    void createScore() {
        chessGameService.startChessGame(new WebBasicBoardStrategy(), 1);
        ScoreDto scoreDto = chessGameService.createScore();
        assertThat(scoreDto.getMessage()).isEqualTo("white : 38.0점\n"
                + "black : 38.0점\n"
                + "무승부 입니다!");
    }

    @Test
    @DisplayName("게임을 시작한 후, 움직임을 체크한다")
    void move() {
        chessGameService.startChessGame(new WebBasicBoardStrategy(), 1);
        GameStatusDto gameStatusDto = chessGameService.move("a2", "a4", 1);
        Map<String, String> board = gameStatusDto.getBoard();
        assertAll(
                () -> assertEquals("white_pawn", board.get("a4")),
                () -> assertNull(board.get("a2"))
        );
    }

    @Test
    @DisplayName("게임을 시작하지 않고, 움직이려고 하면 예외가 발생한다")
    void moveException() {
        assertThatThrownBy(() -> chessGameService.move("a2", "a4", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("체스 게임을 시작해야 합니다.");
    }

    @Test
    @DisplayName("왕이 잡히지 않고, 게임을 종료하게 했을 때 출력문을 검사한다.")
    void end() {
        chessGameService.startChessGame(new WebBasicBoardStrategy(), 1);
        ScoreDto scoreDto = chessGameService.end(1);
        assertThat(scoreDto.getMessage()).isEqualTo("white : 38.0점\n"
                + "black : 38.0점\n"
                + "무승부 입니다!");
    }
}
