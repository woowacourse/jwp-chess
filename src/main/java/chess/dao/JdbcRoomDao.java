package chess.dao;

import chess.dto.RoomDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JdbcRoomDao implements RoomDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<RoomDto> rowMapper = (resultSet, rowid) -> new RoomDto(
            resultSet.getInt(1),
            resultSet.getString(2),
            resultSet.getString(3)
    );

    public JdbcRoomDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createRoom(final String roomName, final String password) {
        final String sql = "insert into room(name, password) values (?, ?)";
        jdbcTemplate.update(sql, roomName, password);
    }

    @Override
    public void deleteRoom(final int roomId, String password) {
        final String sql = "delete from room where id=? and password=?";
        jdbcTemplate.update(sql, roomId, password);

    }

    @Override
    public List<RoomDto> findAllRoom() {
        final String sql = "select id, name, password from room";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
