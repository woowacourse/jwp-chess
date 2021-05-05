package chess.repository;

import chess.domain.web.GameHistory;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GameHistoryDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GameHistory> gameHistoryRowMapper = (resultSet, rowNum) ->
        new GameHistory(
            resultSet.getInt("id"),
            resultSet.getInt("gameId"),
            resultSet.getString("command"),
            resultSet.getTimestamp("createdTime").toLocalDateTime()
        );

    public GameHistoryDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addGameHistory(GameHistory gameHistory) {
        String query = "INSERT INTO game_history(gameId, command, createdTime) VALUES (?, ?, ?)";
        jdbcTemplate.update(query, gameHistory.getGameId(), gameHistory.getCommand(),
            Timestamp.valueOf(gameHistory.getCreatedTime()));
    }

    public List<GameHistory> findAllGameHistoryByGameId(int gameId) {
        String query = "SELECT * FROM game_history gh WHERE gh.gameId = ?";
        return jdbcTemplate.query(query, gameHistoryRowMapper, gameId);
    }
}
