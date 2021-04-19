package chess.dao;


import chess.domain.Room;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {
    private final JdbcTemplate jdbcTemplate;

    public RoomDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long create(Room room) {
        String sql = "insert into room (name, pw, game_id) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"game_id"});
            preparedStatement.setString(1, room.getName());
            preparedStatement.setString(2, room.getPw());
            preparedStatement.setLong(3, room.getGameId());
            return preparedStatement;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Room load(long roomId) {
        String sql = "select * from room where room_id = ?";
        return jdbcTemplate.queryForObject(sql, roomRowMapper, roomId);
    }

    public List<Room> loadAll() {
        String sql = "select * from room";
        return jdbcTemplate.query(sql, roomRowMapper);
    }

    private final RowMapper<Room> roomRowMapper = (resultSet, rowNum) -> {
        long id = resultSet.getLong("room_id");
        String name = resultSet.getString("name");
        String pw = resultSet.getString("pw");
        long gameId = resultSet.getLong("game_id");
        return new Room(id, name, pw, gameId);
    };
}
