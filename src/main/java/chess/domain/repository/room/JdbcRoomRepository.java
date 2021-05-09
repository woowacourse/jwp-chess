package chess.domain.repository.room;

import chess.domain.room.Room;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcRoomRepository implements RoomRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Room> roomRowMapper = (resultSet, rowNum) ->
            new Room(
                    resultSet.getLong("id"),
                    resultSet.getLong("game_id"),
                    resultSet.getString("name"),
                    resultSet.getLong("white_user_id"),
                    resultSet.getLong("black_user_id")
            );

    public JdbcRoomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(final Room room) {
        final String sql = "INSERT INTO room(game_id, name, white_user_id, black_user_id) VALUES(?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, room.gameId());
            ps.setString(2, room.name());
            ps.setLong(3, room.whiteUserId());
            ps.setLong(4, room.blackUserId());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public Long saveNewRoom(final Room room) {
        final String sql = "INSERT INTO room(game_id, name, white_user_id) VALUES(?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, room.gameId());
            ps.setString(2, room.name());
            ps.setLong(3, room.whiteUserId());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void updateBlackUser(final Long userId, final Long roomId) {
        final String sql = "UPDATE room SET black_user_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, userId, roomId);
    }

    @Override
    public Optional<Room> findById(final Long id) {
        final String sql = "SELECT * FROM room WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, roomRowMapper, id));
    }

    @Override
    public Room findByGameId(final Long gameId) {
        final String sql = "SELECT * FROM room WHERE game_id = ?";
        return jdbcTemplate.queryForObject(sql, roomRowMapper, gameId);
    }

    @Override
    public List<Room> findByPlayingGame() {
        final String sql = "SELECT * FROM room AS r " +
                "JOIN game AS g " +
                "ON r.game_id = g.id " +
                "WHERE g.playing = true ORDER BY g.id ASC";
        return jdbcTemplate.query(sql, roomRowMapper);
    }
}
