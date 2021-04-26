package chess.dao;

import chess.dao.dto.GameDto;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class GameDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GameDto> gameRowMapper = (resultSet, rowNum) -> new GameDto(
        resultSet.getLong("id"),
        resultSet.getString("name"),
        resultSet.getLong("host_id"),
        resultSet.getLong("guest_id"),
        resultSet.getString("turn"),
        resultSet.getBoolean("is_finished"),
        resultSet.getTimestamp("created_time").toLocalDateTime()
    );

    public GameDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long insert(final GameDto gameDto) {
        final String sql = "INSERT INTO game(name, host_id, guest_id) VALUES (?, ?, ?)";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final PreparedStatementCreator preparedStatementCreator = con -> {
            final PreparedStatement preparedStatement = con
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, gameDto.getName());
            preparedStatement.setLong(2, gameDto.getHostId());
            preparedStatement.setLong(3, gameDto.getGuestId());
            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public Optional<GameDto> findById(final long gameId) {
        final String sql = "SELECT * FROM game WHERE id = ?";
        return Optional.ofNullable(
            jdbcTemplate.queryForObject(sql, gameRowMapper, gameId)
        );
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
