package chess.service;

import chess.controller.web.dto.MoveRequestDto;
import chess.dao.dto.ChessGame;
import chess.domain.board.Square;
import chess.domain.manager.ChessGameManager;
import chess.domain.manager.ChessGameManagerBundle;
import chess.domain.manager.ChessGameManagerFactory;
import chess.domain.piece.Pawn;
import chess.domain.piece.attribute.Color;
import chess.domain.position.Position;
import chess.domain.statistics.ChessGameStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static chess.domain.piece.attribute.Color.BLACK;
import static chess.domain.piece.attribute.Color.WHITE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ChessServiceImplTest {
    private static final long DEFAULT_CHESS_GAME_ID = 1;

    ChessGame chessGame;
    ChessGameManager sampleGame;

    @Autowired
    ChessServiceImpl chessService;

    @BeforeEach
    void beforeEach() {
        String sample = "RKBQKBKRPPPPPPPP................................pppppppprkbqkbkr"; // move a2 a3 한 번 진행
        chessGame = new ChessGame(DEFAULT_CHESS_GAME_ID, WHITE, true, sample, "temp");
        sampleGame = ChessGameManagerFactory.loadingGame(chessGame);
    }

    @Test
    @DisplayName("체스 게임을 시작한다.")
    void start() {
        ChessGameManager chessGameManager = chessService.start("");

        assertThat(chessGameManager.getId()).isEqualTo(2);
    }

    @Test
    @DisplayName("체스 게임을 끝낸다.")
    void end() {
        ChessGameManager chessGameManager = chessService.end(DEFAULT_CHESS_GAME_ID);

        assertThat(chessGameManager.isEnd()).isTrue();
    }

    @Test
    @DisplayName("진행중인 체스 게임들을 찾는다.")
    void findRunningGames() {
        ChessGameManagerBundle runningGames = chessService.findRunningGames();

        assertThat(runningGames.getIdAndNextTurn().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("id로 체스 게임을 불러온다.")
    void load() {
        ChessGameManager chessGameManager = chessService.load(DEFAULT_CHESS_GAME_ID);

        assertThat(chessGameManager.getId()).isEqualTo(DEFAULT_CHESS_GAME_ID);
    }

    @Test
    @DisplayName("체스 게임 보드를 움직인다.")
    void move() {
        chessService.move(DEFAULT_CHESS_GAME_ID, new MoveRequestDto("h7", "h6"));

        ChessGameManager chessGameManager = chessService.findById(DEFAULT_CHESS_GAME_ID);
        Square h6 = chessGameManager.getBoard().findByPosition(Position.of("h6"));
        assertThat(h6.getPiece().getClass()).isEqualTo(Pawn.class);
        assertThat(h6.getPiece().getColor()).isEqualTo(BLACK);
    }

    @Test
    @DisplayName("게임이 끝났는지 확인한다.")
    void isEnd() {
        boolean end = chessService.isEnd(DEFAULT_CHESS_GAME_ID);
        assertThat(end).isFalse();
    }

    @Test
    @DisplayName("id로 체스 게임을 찾는다.")
    void findById() {
        ChessGameManager chessGameManager = chessService.findById(DEFAULT_CHESS_GAME_ID);

        assertThat(chessGameManager.getId()).isEqualTo(DEFAULT_CHESS_GAME_ID);
    }

    @Test
    @DisplayName("다음 턴을 가져온다.")
    void nextColor() {
        Color color = chessService.nextColor(DEFAULT_CHESS_GAME_ID);

        assertThat(color).isEqualTo(BLACK);
    }

    @Test
    @DisplayName("첨수를 가져온다.")
    void getStatistics() {
        ChessGameStatistics statistics = chessService.getStatistics(DEFAULT_CHESS_GAME_ID);

        assertThat(statistics.getColorsScore().size()).isEqualTo(2);
    }
}