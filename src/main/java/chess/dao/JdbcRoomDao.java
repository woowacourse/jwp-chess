package chess.dao;

import chess.dto.RoomDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcRoomDao implements RoomDao {

    private final RowMapper<RoomDto> roomRowMapper = (resultSet, rowNum) -> {
        return new RoomDto(
                resultSet.getString("name"),
                resultSet.getString("password")
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcRoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void makeRoom(final RoomDto roomDto) {
        final String sql = "insert into room (name, password) values (?, ?)";
        jdbcTemplate.update(sql, roomDto.getName(), roomDto.getPassword());
    }

    @Override
    public long findIdByRoomName(final String name) {
        final String sql = "select id from room where name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, long.class, name);
        } catch (Exception e) {
            throw new IllegalArgumentException("해당 방이 존재하지 않습니다.");
        }
    }

    @Override
    public RoomDto findRoomById(final long id) {
        final String sql = "select name, password from room where id = ?";
        return jdbcTemplate.queryForObject(sql, roomRowMapper, id);
    }

    @Override
    public void deleteRoom(final long id) {
        final String sql = "delete from room where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
