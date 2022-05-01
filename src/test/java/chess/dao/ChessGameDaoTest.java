package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
        assertDoesNotThrow(() -> chessGameDao.saveChessGame("게임1", "1234", "화이트"));
    }

    @Test
    @DisplayName("중복된 게임 이름은 저장할 수 없다.")
    void duplicateSaveChessGameName() {
        final String gameName = "게임1";
        chessGameDao.saveChessGame(gameName, "1234", "화이트");
        assertThatThrownBy(() -> chessGameDao.saveChessGame(gameName, "1234", "화이트"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 게임 이름입니다.");
    }

    @Test
    @DisplayName("저장한 체스 게임 번호를 조회한다.")
    void findChessGameIdByName() {
        chessGameDao.saveChessGame("게임1", "1234", "화이트");
        final int expected = 1;

        final int actual = chessGameDao.findChessGameIdByName("게임1");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("현재 턴 정보를 불러온다.")
    void findCurrentTurn() {
        final String expected = "화이트";
        chessGameDao.saveChessGame("게임1", "1234", expected);
        final int gameId = 1;

        final String actual = chessGameDao.findCurrentTurn(gameId);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("게임을 삭제한다.")
    void deleteChessGame() {
        chessGameDao.saveChessGame("게임1", "1234", "화이트");
        chessGameDao.saveChessGame("게임2", "1234", "화이트");
        final int expected = 1;

        chessGameDao.deleteChessGame(2);

        final int actual = chessGameDao.findAllChessGame().size();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("현재 턴 정보를 업데이트한다.")
    void updateGameTurn() {
        chessGameDao.saveChessGame("게임1", "1234", "화이트");
        final String expected = Team.BLACK.getName();

        chessGameDao.updateGameTurn(1, Team.BLACK);
        final String actual = chessGameDao.findChessGame(1).getTurn();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("모든 체스 게임을 찾는다.")
    void findAllChessGame() {
        chessGameDao.saveChessGame("게임1", "1234", "화이트");
        chessGameDao.saveChessGame("게임2", "1234", "화이트");

        final int actual = chessGameDao.findAllChessGame().size();

        assertThat(actual).isEqualTo(2);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 확인한다.")
    void findChessGameByIdAndPassword() {
        chessGameDao.saveChessGame("게임1", "1234", "화이트");

        final int actual = chessGameDao.findChessGameByIdAndPassword(1, "1234");

        assertThat(actual).isEqualTo(1);
    }

    @Test
    @DisplayName("번호로 게임을 찾는다.")
    void findChessGame() {
        chessGameDao.saveChessGame("게임1", "1234", "화이트");

        final ChessGameInfoDto chessGame = chessGameDao.findChessGame(1);

        assertAll(() -> {
                    assertThat(chessGame.getId()).isEqualTo(1);
                    assertThat(chessGame.getName()).isEqualTo("게임1");
                    assertThat(chessGame.getTurn()).isEqualTo("화이트");
                }
        );
    }
}
