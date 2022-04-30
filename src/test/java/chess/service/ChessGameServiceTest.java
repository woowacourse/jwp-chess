package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.model.board.BoardFactory;
import chess.model.dao.GameDao;
import chess.model.dao.PieceDao;
import chess.model.dto.WebBoardDto;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql({"/initGames.sql", "/initPieces.sql"})
class ChessGameServiceTest {
    private final ChessGameService chessGameService;
    private final GameDao gameDao;
    private final PieceDao pieceDao;
    private Long gameId;

    @Autowired
    ChessGameServiceTest(JdbcTemplate jdbcTemplate) {
        gameDao = new GameDao(jdbcTemplate);
        pieceDao = new PieceDao(jdbcTemplate);

        chessGameService = new ChessGameService(
                pieceDao, gameDao, new ChessBoardService(pieceDao, gameDao));
    }

    @BeforeEach
    void beforeEach() {
        gameId = gameDao.saveGame("serviceTestGame", "pw123");
        pieceDao.savePieces(BoardFactory.create(), gameId);
    }

    @Test
    @DisplayName("DB에 저장된 모든 게임들의 gameId와 방 타이틀을 가져오는 테스트")
    void getAllGamesTest() {
        Map<Long, String> games = chessGameService.getAllGamesWithIdAndTitle();

        assertThat(games.keySet()).containsExactly(1L, 2L);
        assertThat(games.values()).containsExactly("title1", "serviceTestGame");
    }

    @Test
    @DisplayName("새 게임을 생성하는 테스트")
    void createNewGameTest() {
        gameId = chessGameService.createGame("newGame", "pw1234");

        assertThat(chessGameService.getAllGamesWithIdAndTitle().keySet())
                .contains(gameId);
    }

    @Test
    @DisplayName("이전에 저장된 게임을 불러오는 테스트")
    void continueGameTest() {
        WebBoardDto webBoardDto = chessGameService.continueGame(gameId);

        assertThat(webBoardDto.getWebBoard().entrySet().size()).isEqualTo(64);
    }

    @Test
    @DisplayName("게임 종료 샹태갸 아닐 때 방을 삭제하려는 경우 테스트")
    void deleteGameWhilePlaying() {
        assertThatThrownBy(() -> {
            chessGameService.deleteGame(gameId, "pw1234");
        })
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("게임이 진행중인 방은 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("방 삭제 시 방 비밀번호가 틀린 경우 테스트")
    void deleteGameWithWrongPw() {
        gameDao.updateTurnByGameId(gameId, "end");

        assertThatThrownBy(() -> {
            chessGameService.deleteGame(gameId, "worngPw");
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("방 비밀번호가 맞지 않습니다.");
    }

    @Test
    @DisplayName("방 삭제 테스트")
    void deleteGameTest() {
        gameDao.updateTurnByGameId(gameId, "end");
        chessGameService.deleteGame(gameId, "pw123");

        assertThat(chessGameService.getAllGamesWithIdAndTitle().keySet()).doesNotContain(gameId);
    }
}