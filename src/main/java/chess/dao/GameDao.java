package chess.dao;

import chess.domain.game.Game;
import chess.domain.team.Team;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Game> gameRowMapper = (resultSet, rowNum) -> new Game(
        resultSet.getLong("id"),
        resultSet.getString("name"),
        resultSet.getLong("host_id"),
        resultSet.getLong("guest_id"),
        Team.from(resultSet.getString("turn")),
        resultSet.getBoolean("is_finished"),
        resultSet.getTimestamp("created_time").toLocalDateTime()
    );

    public GameDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long insert(final Game game) {
        final String sql = "INSERT INTO game(name, host_id, guest_id) VALUES (?, ?, ?)";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final PreparedStatementCreator preparedStatementCreator = con -> {
            final PreparedStatement preparedStatement = con
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, game.getName());
            preparedStatement.setLong(2, game.getHostId());
            preparedStatement.setLong(3, game.getGuestId());
            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public Game findById(final long gameId) {
        final String sql = "SELECT * FROM game WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, gameRowMapper, gameId);
    }

    public void updateGameStatus(final long id, final boolean isFinished) {
        final String sql = "UPDATE game SET is_finished = ? WHERE id = ?";
        jdbcTemplate.update(sql, isFinished, id);
    }

    public void updateTurn(final long id) {
        final String sql = "UPDATE game SET turn = (CASE WHEN turn = 'black' THEN 'white' ELSE 'black' END) WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}
