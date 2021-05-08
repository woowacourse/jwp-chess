package chess.dao;

import chess.dao.dto.RoomDto;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<RoomDto> roomRowMapper = (resultSet, rowNum) -> RoomDto.of(
        resultSet.getLong("id"),
        resultSet.getLong("game_id"),
        resultSet.getLong("host_id"),
        (Long) resultSet.getObject("guest_id"),
        resultSet.getString("name")
    );

    public RoomDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long insert(final RoomDto roomDto) {
        final String sql = "INSERT INTO room(game_id, host_id, name) VALUES (?, ?, ?)";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        final PreparedStatementCreator preparedStatementCreator = con -> {
            final PreparedStatement preparedStatement = con
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, roomDto.getGameId());
            preparedStatement.setLong(2, roomDto.getHostId());
            preparedStatement.setString(3, roomDto.getName());
            return preparedStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public RoomDto selectByGameId(final long gameId) {
        final String sql = "SELECT * FROM room WHERE game_id = ?";
        return jdbcTemplate.queryForObject(sql, roomRowMapper, gameId);
    }

    public RoomDto selectIdById(final long id) {
        final String sql = "SELECT * FROM room WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, roomRowMapper, id);
    }

    public List<RoomDto> selectBatchWithEmptyGuest() {
        final String sql = "SELECT * FROM room WHERE guest_id IS NULL";
        return jdbcTemplate.query(sql, roomRowMapper);
    }

    public void updateGuestById(final long guestId, final long id) {
        final String sql = "UPDATE room SET guest_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, guestId, id);
    }

}
