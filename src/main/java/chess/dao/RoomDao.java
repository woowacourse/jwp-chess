package chess.dao;

import chess.domain.Room;
import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {
    private final JdbcTemplate jdbcTemplate;

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Room> selectAll() {
        final String sql = "select no, title from room";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> new Room(
                resultSet.getLong("no"),
                resultSet.getString("title"))
        );
    }

    public long insert(Room room) {
        final String sql = "insert into room (title, password) values (?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"no"});
            ps.setString(1, room.getTitle());
            ps.setString(2, room.getPassword());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public String loadPassword(long roomNo) {
        final String sql = "select password from room where no = ?";
        return jdbcTemplate.queryForObject(sql, String.class, roomNo);
    }

    public String loadTitle(long roomNo) {
        final String sql = "select title from room where no = ?";
        return jdbcTemplate.queryForObject(sql, String.class, roomNo);
    }

    public void delete(long roomNo) {
        final String sql = "delete from room where no = ?";
        jdbcTemplate.update(sql, roomNo);
    }
}
