package chess.dao;

import chess.dto.RoomDto;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcRoomDao implements RoomDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<RoomDto> roomRowMapper = (resultSet, rowNum) -> {
        return new RoomDto(
                resultSet.getInt("id"),
                resultSet.getString("title")
        );
    };

    public JdbcRoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void createRoom(RoomDto room) {
        String sql = "insert into room (title, password) values (?, ?)";
        jdbcTemplate.update(sql, room.getTitle(), room.getPassword());
    }

    @Override
    public int getRecentCreatedRoomId() {
        String sql = "select id from room order by id desc limit 1";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public boolean matchPassword(int id, String password) {
        String sql = "select count(*) from room where id = ? and password = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id, password) == 1;
    }

    @Override
    public List<RoomDto> getRooms() {
        String sql = "select id, title from room";
        return jdbcTemplate.query(sql, roomRowMapper);
    }

    @Override
    public void deleteRoom(RoomDto roomDto) {
        String sql = "delete from room where id = ? and password = ?";
        jdbcTemplate.update(sql, roomDto.getId(), roomDto.getPassword());
    }
}
