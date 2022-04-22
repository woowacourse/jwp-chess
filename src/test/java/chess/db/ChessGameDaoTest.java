package chess.db;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import chess.domain.ChessGame;
import chess.domain.GameTurn;
import chess.domain.board.InitialBoardGenerator;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class ChessGameDaoTest {
    private final static String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/chess";
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private org.springframework.jdbc.datasource.DriverManagerDataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void deleteTable() {
        String deletePieceSql = "delete from piece where gameID = 'test'";
        jdbcTemplate.update(deletePieceSql);

        String deleteChessGameSql = "delete from chessGame where gameID = 'test'";
        jdbcTemplate.update(deleteChessGameSql);
    }

    @DisplayName("존재하는 게임에 대한 검색은 예외를 반환하지 않는다")
    @Test
    void findTurnByID() {

        dataSource = new org.springframework.jdbc.datasource.DriverManagerDataSource(URL, USER, PASSWORD);
        dataSource.setDriverClassName(DRIVER);
        jdbcTemplate = new JdbcTemplate(dataSource);
        ChessGameDao chessGameDao = new ChessGameDao(jdbcTemplate);
        ChessGame chessGame = new ChessGame(new InitialBoardGenerator(), GameTurn.BLACK);
        chessGameDao.save("test", chessGame);

        assertThat(chessGameDao.findTurnByID("test")).isEqualTo("BLACK");
    }
}
