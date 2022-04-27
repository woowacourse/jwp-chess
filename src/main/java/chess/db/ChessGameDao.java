package chess.db;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.domain.ChessGame;

@Repository
public class ChessGameDao {
    private final JdbcTemplate jdbcTemplate;

    public ChessGameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(String gameID, ChessGame chessGame) {
        String sql = "insert into chessGame (gameID, turn) values (?, ?)";
        jdbcTemplate.update(sql, gameID, chessGame.getTurn().name());
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
        final List<String> turns = jdbcTemplate.queryForList(sql, String.class, gameID);
        return turns.get(0);
    }

    public List<String> findAllGame() {
        String sql = "select gameID from chessGame";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> resultSet.getString("gameID"));
    }

    public void deleteByGameID(String gameID) {
        String sql = "delete from chessGame where gameID = ?";
        jdbcTemplate.update(sql, gameID);
    }
}
