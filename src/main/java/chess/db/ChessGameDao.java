package chess.db;

import chess.domain.ChessGame;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChessGameDao {
    private final static String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/chess";
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private org.springframework.jdbc.datasource.DriverManagerDataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public ChessGameDao() {
        dataSource = new org.springframework.jdbc.datasource.DriverManagerDataSource(URL, USER, PASSWORD);
        dataSource.setDriverClassName(DRIVER);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void save(String gameID, ChessGame chessGame) {
        String sql = "insert into chessGame (gameID, turn) values (?, ?)";
        jdbcTemplate.update(sql, gameID, chessGame.getTurn().name());
    }

    public void updateTurn(String gameID, ChessGame chessGame) {
        String sql = "update chessGame set turn = ? where gameID = ?";
        jdbcTemplate.update(sql, chessGame.getTurn().name(), gameID);
    }

    public String findTurnByID(String gameID) {
        String sql = "select turn from chessGame where gameID = ?";
        final List<String> turns = jdbcTemplate.queryForList(sql, String.class, gameID);
        return turns.get(0);
    }
}
