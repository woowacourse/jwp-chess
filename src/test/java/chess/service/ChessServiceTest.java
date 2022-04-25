package chess.service;

import static org.assertj.core.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import chess.db.ChessGameDao;
import chess.db.PieceDao;
import chess.domain.ChessGame;
import chess.domain.GameTurn;
import chess.domain.board.InitialBoardGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class ChessServiceTest {
    private final static String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/chess";
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private org.springframework.jdbc.datasource.DriverManagerDataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void initTable() {
        dataSource = new org.springframework.jdbc.datasource.DriverManagerDataSource(URL, USER, PASSWORD);
        dataSource.setDriverClassName(DRIVER);
        jdbcTemplate = new JdbcTemplate(dataSource);

        ChessGameDao chessGameDao = new ChessGameDao(jdbcTemplate);
        chessGameDao.save("test", new ChessGame(new InitialBoardGenerator(), GameTurn.WHITE));

        PieceDao pieceDao = new PieceDao(jdbcTemplate);
        pieceDao.save("test");
    }

    @AfterEach
    void deleteTable() {
        String deletePieceSql = "delete from piece where gameID = 'test'";
        jdbcTemplate.update(deletePieceSql);

        String deleteChessGameSql = "delete from chessGame where gameID = 'test'";
        jdbcTemplate.update(deleteChessGameSql);
    }

    @Test
    @DisplayName("gameID를 이용해 DB로부터 불러온 turn이 해당 gameTurn과 일치한다")
    void getTurn() {
        ChessService dbService = new ChessService(new ChessGameDao(jdbcTemplate), new PieceDao(jdbcTemplate));
        assertThat(dbService.getTurn("test")).isEqualTo(GameTurn.WHITE);
    }
}
