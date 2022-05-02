package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.player.Team;
import chess.dto.response.ChessGameInfoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ChessGameDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ChessGameDao chessGameDao;

    @BeforeEach
    void setUp() {
        chessGameDao = new ChessGameDao(jdbcTemplate);
    }

    @Test
    @DisplayName("체스 게임을 저장한다.")
    void saveChessGame() {
        final int gameId = saveGame("슬로", "1234", "화이트");
        final ChessGameInfoDto chessGame = chessGameDao.findChessGame(gameId);
        assertAll(() -> {
            assertThat(chessGame.getId()).isEqualTo(gameId);
            assertThat(chessGame.getName()).isEqualTo("슬로");
            assertThat(chessGame.getTurn()).isEqualTo("화이트");
        });
    }

    @Test
    @DisplayName("저장한 체스 게임 번호를 조회한다.")
    void findChessGameIdByName() {
        final int gameId = saveGame("슬로", "1234", "화이트");
        final int actual = chessGameDao.findChessGameIdByName("슬로");

        assertThat(actual).isEqualTo(gameId);
    }

    @Test
    @DisplayName("현재 턴 정보를 불러온다.")
    void findCurrentTurn() {
        final String expected = "화이트";
        final int gameId = saveGame("게임1", "1234", expected);

        final String actual = chessGameDao.findCurrentTurn(gameId);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("게임을 삭제한다.")
    void deleteChessGame() {
        final int gameId = saveGame("슬로", "1234", "화이트");
        final int expected = 0;

        chessGameDao.deleteChessGame(gameId);

        final int actual = chessGameDao.findAllChessGame().size();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("현재 턴 정보를 업데이트한다.")
    void updateGameTurn() {
        final int gameId = saveGame("게임1", "1234", "화이트");
        final String expected = Team.BLACK.getName();

        chessGameDao.updateGameTurn(gameId, Team.BLACK);
        final String actual = chessGameDao.findCurrentTurn(gameId);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("모든 체스 게임을 찾는다.")
    void findAllChessGame() {
        saveGame("슬로", "1234", "화이트");
        final int actual = chessGameDao.findAllChessGame().size();

        assertThat(actual).isEqualTo(1);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 확인한다.")
    void findChessGameByIdAndPassword() {
        final int gameId = saveGame("슬로", "1234", "화이트");
        final int actual = chessGameDao.findChessGameByIdAndPassword(gameId, "1234");

        assertThat(actual).isEqualTo(1);
    }

    @Test
    @DisplayName("번호로 게임을 찾는다.")
    void findChessGame() {
        final int gameId = saveGame("슬로", "1234", "화이트");
        final ChessGameInfoDto chessGame = chessGameDao.findChessGame(gameId);

        assertAll(() -> {
                    assertThat(chessGame.getId()).isEqualTo(gameId);
                    assertThat(chessGame.getName()).isEqualTo("슬로");
                    assertThat(chessGame.getTurn()).isEqualTo("화이트");
                }
        );
    }

    private int saveGame(String gameName, String password, String turn) {
        return chessGameDao.saveChessGame(gameName, password, turn);
    }
}
