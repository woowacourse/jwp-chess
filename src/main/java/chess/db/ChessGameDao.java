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

    public void save(String gameID, String password, ChessGame chessGame) {
        String sql = "insert into chessGame (gameID, password, turn) values (?, ?, ?)";
        jdbcTemplate.update(sql, gameID, password, chessGame.getTurn().name());
    }

    public void updateTurn(String gameID, ChessGame chessGame) {
        String sql = "update chessGame set turn = ? where gameID = ?";
        jdbcTemplate.update(sql, chessGame.getTurn().name(), gameID);
    }

    public void initTurn(String gameID) {
        String sql = "update chessGame set turn = ? where gameID = ?";
        jdbcTemplate.update(sql, "WHITE", gameID);
    }

    public String findTurnByID(String gameID) {
        String sql = "select turn from chessGame where gameID = ?";
        return jdbcTemplate.queryForObject(sql, String.class, gameID);
    }

    public List<String> findAllGame() {
        String sql = "select gameID from chessGame";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getString("gameID"));
    }

    public void deleteByGameID(String gameID, String password) {
        String sql = "delete from chessGame where gameID = ? and password = ?";
        jdbcTemplate.update(sql, gameID, password);
    }

    public boolean findPasswordByGameID(String gameID, String password) {
        String sql = "select password from chessGame where gameID = ?";
        if (password == null) {
            return false;
        }
        try {
            String result = jdbcTemplate.queryForObject(sql, String.class, gameID);
            return password.equals(result);
        } catch (IncorrectResultSizeDataAccessException e) {
            return false;
        }
    }
}
