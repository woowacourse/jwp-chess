package chess.repository;

import chess.domain.web.Game;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Game> gameRowMapper = (resultSet, rowNum) ->
        new Game(
            resultSet.getInt("id"),
            resultSet.getInt("userId"),
            resultSet.getBoolean("isEnd"),
            resultSet.getTimestamp("createdTime").toLocalDateTime()
        );

    public GameDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int addGame(Game game) {
        String query = "INSERT INTO game(userId, isEnd, createdTime) VALUES (?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection
                .prepareStatement(query, new String[]{"id"});
            preparedStatement.setInt(1, game.getUserId());
            preparedStatement.setBoolean(2, game.isEnd());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(game.getCreatedTime()));
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public List<Game> findGamesByUserId(int userId) {
        String query = "SELECT * FROM game g WHERE g.userId = ?";
        return jdbcTemplate.query(query, gameRowMapper, userId);
    }

    public void updateGameIsEnd(int gameId) {
        String query = "UPDATE game SET isEnd = true WHERE id = ?";
        jdbcTemplate.update(query, gameId);
    }
}
