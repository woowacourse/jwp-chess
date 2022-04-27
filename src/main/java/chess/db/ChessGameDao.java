package chess.db;

import chess.domain.ChessGame;
import chess.domain.GameEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;

@Repository
public class ChessGameDao {
    private JdbcTemplate jdbcTemplate;

    public ChessGameDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(String gameID, String gamePW, ChessGame chessGame) {
        String sql = "insert into chessGame (gameID, gamePW, gameCode, turn) values (?, ?, ?, ?)";
        String gameCode = "hash" + gameID + gamePW + "val";
        jdbcTemplate.update(sql, gameID, gamePW, gameCode, chessGame.getTurn().name());
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

    public boolean checkPassword(String gameID, String inputPW) {
        String sql = "select gamePW from chessGame where gameID = ?";
        final List<String> passwords = jdbcTemplate.queryForList(sql, String.class, gameID);
        String gamePW = passwords.get(0);
        return inputPW.equals(gamePW);
    }

    public List<GameEntity> findAllGames() {
        String sql = "select gameID, gameCode, turn from chessGame";
        return jdbcTemplate.query(sql, new GameMapper());
    }

    public String findIDByCode(String gameCode) {
        String sql = "select gameID from chessGame where gameCode = ?";
        final List<String> gameIDs = jdbcTemplate.queryForList(sql, String.class, gameCode);
        return gameIDs.get(0);
    }

    private static final class GameMapper implements RowMapper {
        public Object mapRow(ResultSet rs, int rowCnt) throws SQLException {
            GameEntity gameEntry = new GameEntity();
            gameEntry.setGameID(rs.getString("gameID"));
            gameEntry.setGameCode(rs.getString("gameCode"));
            gameEntry.setIsFinished(rs.getString("turn"));
            return gameEntry;
        }

    }
}
