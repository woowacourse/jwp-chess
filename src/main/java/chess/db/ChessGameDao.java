package chess.db;

import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.domain.ChessGame;

@Repository
public class ChessGameDao {
    private final JdbcTemplate jdbcTemplate;

    public ChessGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(String gameId, String password, ChessGame chessGame) {
        String sql = "insert into chessGame (gameId, password, turn) values (?, ?, ?)";
        jdbcTemplate.update(sql, gameId, password, chessGame.getTurn().name());
    }

    public void updateTurn(String gameId, ChessGame chessGame) {
        String sql = "update chessGame set turn = ? where gameId = ?";
        jdbcTemplate.update(sql, chessGame.getTurn().name(), gameId);
    }

    public void initTurn(String gameId) {
        String sql = "update chessGame set turn = ? where gameId = ?";
        jdbcTemplate.update(sql, "WHITE", gameId);
    }

    public String findTurnById(String gameId) {
        String sql = "select turn from chessGame where gameId = ?";
        return jdbcTemplate.queryForObject(sql, String.class, gameId);
    }

    public List<String> findAllGame() {
        String sql = "select gameId from chessGame";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getString("gameId"));
    }

    public void deleteByGameId(String gameId, String password) {
        String sql = "delete from chessGame where gameId = ? and password = ?";
        jdbcTemplate.update(sql, gameId, password);
    }

    public boolean findPasswordByGameId(String gameId, String password) {
        String sql = "select password from chessGame where gameId = ?";
        if (password == null) {
            return false;
        }
        try {
            String result = jdbcTemplate.queryForObject(sql, String.class, gameId);
            return password.equals(result);
        } catch (IncorrectResultSizeDataAccessException e) {
            return false;
        }
    }
}
