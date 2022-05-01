package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.exception.IllegalDeleteRoomException;
import chess.model.board.BoardFactory;
import chess.model.dao.GameDao;
import chess.model.dao.PieceDao;
import chess.model.dto.WebBoardDto;
import chess.model.piece.Piece;
import chess.model.position.Position;
import java.util.ArrayList;
import java.util.List;
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

    private final JdbcTemplate jdbcTemplate;
    private final ChessGameService chessGameService;
    private Long gameId = 1L;

    @Autowired
    ChessGameServiceTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        chessGameService = new ChessGameService(new PieceDao(jdbcTemplate), new GameDao(jdbcTemplate));
    }

    @BeforeEach
    void beforeEach() {
        gameId = chessGameService.createGame("serviceTestRoom", "pw1234");
    }

    @Test
    @DisplayName("DB에 저장된 모든 게임들의 gameId와 방 타이틀을 가져오는 테스트")
    void getAllGamesTest() {
        Map<Long, String> games = chessGameService.getAllGamesWithIdAndTitle();

        assertThat(games.keySet()).containsExactly(1L, 2L);
        assertThat(games.values()).containsExactly("title1", "serviceTestRoom");
    }

    @Test
    @DisplayName("새 게임을 생성하는 테스트")
    void createNewGameTest() {
        Long newGameId = chessGameService.createGame("newGame", "pw1234");

        assertThat(chessGameService.getAllGamesWithIdAndTitle().keySet())
                .contains(newGameId);
    }

    @Test
    @DisplayName("이전에 저장된 게임을 불러오는 테스트")
    void continueGameTest() {
        WebBoardDto webBoardDto = chessGameService.continueGame(gameId);

        assertThat(webBoardDto.getWebBoard().entrySet().size()).isEqualTo(64);
    }

    @Test
    @DisplayName("게임의 현재 턴을 반환받는 테스트")
    void getTurnTest() {
        String turn = chessGameService.getTurn(gameId);

        assertThat(turn).isEqualTo("white");
    }

    @Test
    @DisplayName("게임 종료 샹태갸 아닐 때 방을 삭제하려는 경우 테스트")
    void deleteGameWhilePlaying() {
        assertThatThrownBy(() -> chessGameService.deleteGame(gameId, "pw1234"))
                .isInstanceOf(IllegalDeleteRoomException.class)
                .hasMessageContaining("게임이 진행중인 방은 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("방 삭제 시 방 비밀번호가 틀린 경우 테스트")
    void deleteGameWithWrongPw() {
        chessGameService.exitGame(gameId);

        assertThatThrownBy(() -> chessGameService.deleteGame(gameId, "wrongPw"))
                .isInstanceOf(IllegalDeleteRoomException.class)
                .hasMessageContaining("방 비밀번호가 맞지 않습니다.");
    }

    @Test
    @DisplayName("방 삭제 테스트")
    void deleteGameTest() {
        chessGameService.exitGame(gameId);
        chessGameService.deleteGame(gameId, "pw1234");

        assertThat(chessGameService.getAllGamesWithIdAndTitle().keySet()).doesNotContain(gameId);
    }


    @Test
    @DisplayName("게임 종료 테스트")
    void exitGameTest() {
        chessGameService.exitGame(gameId);

        assertThat(chessGameService.getTurn(gameId)).isEqualTo("end");
    }

    @Test
    @DisplayName("게임 재시작 테스트")
    void restartGameTest() {
        chessGameService.restartGame(gameId);
        WebBoardDto board = chessGameService.continueGame(gameId);

        assertThat(board.getWebBoard()).isEqualTo(WebBoardDto.from(BoardFactory.create()).getWebBoard());
        assertThat(chessGameService.getTurn(gameId)).isEqualTo("white");
    }
}