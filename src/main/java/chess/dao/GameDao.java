package chess.dao;

import chess.dao.dto.GameDto;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GameDto> gameRowMapper = (resultSet, rowNum) -> GameDto.of(
        resultSet.getLong("id"),
        resultSet.getString("turn"),
        resultSet.getBoolean("is_finished"),
        resultSet.getTimestamp("created_time").toLocalDateTime()
    );

    public GameDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long insert() {
        final String sql = "INSERT INTO game VALUES ()";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final PreparedStatementCreator preparedStatementCreator = con -> {
            final PreparedStatement preparedStatement =
                con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public GameDto findById(final long gameId) {
        final String sql = "SELECT * FROM game WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, gameRowMapper, gameId);
    }

    public List<GameDto> findByStatus(final boolean isFinished) {
        final String sql = "SELECT * FROM game WHERE is_finished = ?";
        return jdbcTemplate.query(sql, gameRowMapper, isFinished);
    }

    public void updateGameStatus(final long id, final boolean isFinished) {
        final String sql = "UPDATE game SET is_finished = ? WHERE id = ?";
        jdbcTemplate.update(sql, isFinished, id);
    }

    public void updateTurn(final long id, final String turn) {
        final String sql = "UPDATE game SET turn = ? WHERE id = ?";
        jdbcTemplate.update(sql, turn, id);
    }

    public void reverseTurn(final long id) {
        final String sql = "UPDATE game SET turn = (CASE WHEN turn = 'black' THEN 'white' ELSE 'black' END) WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}
