package chess.db;

import chess.domain.ChessGame;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChessGameDao {
    private JdbcTemplate jdbcTemplate;

    public ChessGameDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(String gameID, String gamePW, ChessGame chessGame) {
        String sql = "insert into chessGame (gameID, gamePW, hashValue, turn) values (?, ?, ?, ?)";
        String hashValue = "hash" + gameID + gamePW + "val";
        jdbcTemplate.update(sql, gameID, gamePW, hashValue, chessGame.getTurn().name());
    }

    public void delete(String gameID) {
        String sql = "delete from chessGame where gameID = ?";
        jdbcTemplate.update(sql, gameID);
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

    public boolean checkPassword(String gameID, String inputPW){
        String sql = "select gamePW from chessGame where gameID = ?";
        final List<String> passwords = jdbcTemplate.queryForList(sql, String.class, gameID);
        String gamePW = passwords.get(0);
        return inputPW.equals(gamePW);
    }

    public List<String> findAllGames() {
        String sql = "select gameID from chessGame";
        return jdbcTemplate.queryForList(sql, String.class);
    }
}
