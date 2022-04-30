package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.model.GameResult;
import chess.model.Team;
import chess.model.board.Board;
import chess.model.board.BoardFactory;
import chess.model.dao.GameDao;
import chess.model.dao.PieceDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql({"/initGames.sql", "/initPieces.sql"})
class ChessBoardServiceTest {

    private final ChessBoardService chessBoardService;
    private final GameDao gameDao;
    private final PieceDao pieceDao;
    private Long gameId;

    @Autowired
    ChessBoardServiceTest(JdbcTemplate jdbcTemplate) {
        gameDao = new GameDao(jdbcTemplate);
        pieceDao = new PieceDao(jdbcTemplate);

        chessBoardService = new ChessBoardService(pieceDao, gameDao);
    }

    @BeforeEach
    void beforeEach() {
        gameId = gameDao.saveGame("serviceTestGame", "pw123");
        pieceDao.savePieces(BoardFactory.create(), gameId);
    }

    @Test
    @DisplayName("엔티티로 받은 데이터를 Board로 변환하는 테스트")
    void toBoardTest() {
        Board board = BoardFactory.create();
        Board resultBoard = chessBoardService.toBoard(pieceDao.findAllPiecesByGameId(gameId));

        assertThat(resultBoard).isEqualTo(board);
    }

    @Test
    @DisplayName("게임의 현재 턴을 반환받는 테스트")
    void getTurnTest() {
        String turn = chessBoardService.getTurn(gameId);

        assertThat(turn).isEqualTo("white");
    }

    @Test
    @DisplayName("왕의 생존 여부를 반환받는 테스트")
    void isKingDeadTest() {
        boolean isKingDead = chessBoardService.isKingDead(gameId);

        assertThat(isKingDead).isFalse();
    }

    @Test
    @DisplayName("게임 결과 반환 테스트")
    void getGameResultTest() {
        GameResult gameResult = chessBoardService.getResult(gameId);

        assertThat(gameResult.getWhiteScore()).isEqualTo(38);
        assertThat(gameResult.getBlackScore()).isEqualTo(38);
        assertThat(gameResult.getWinningTeam()).isEqualTo(Team.NONE);
    }

    @Test
    @DisplayName("게임 종료 테스트")
    void exitGameTest() {
        chessBoardService.exitGame(gameId);

        assertThat(gameDao.findTurnByGameId(gameId).get()).isEqualTo("end");
    }

    @Test
    @DisplayName("게임 재시작 테스트")
    void restartGameTest() {
        chessBoardService.restartGame(gameId);

        assertThat(chessBoardService.toBoard(pieceDao.findAllPiecesByGameId(gameId)))
                .isEqualTo(BoardFactory.create());
        assertThat(gameDao.findTurnByGameId(gameId).get())
                .isEqualTo("white");
    }
}