package chess.dao;

import chess.dto.RoomDto;
import chess.dto.RoomRequestDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class JdbcRoomDao implements RoomDao {

    private final RowMapper<RoomDto> roomRowMapper = (resultSet, rowNum) -> {
        return new RoomDto(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("password")
        );
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcRoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long makeRoom(final RoomRequestDto roomRequestDto) {
        final String sql = "insert into room (name, password) values (?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, roomRequestDto.getName());
            ps.setString(2, roomRequestDto.getPassword());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public List<RoomDto> findAll() {
        final String sql = "select * from room";
        return jdbcTemplate.query(sql, roomRowMapper);
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
        final String sql = "select * from room where id = ?";
        return jdbcTemplate.queryForObject(sql, roomRowMapper, id);
    }

    @Override
    public void deleteRoom(final long id) {
        final String sql = "delete from room where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
