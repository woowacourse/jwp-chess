package chess.dao;

import chess.dto.response.RoomResponse;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<RoomResponse> actorRowMapper = (resultSet, rowNum) -> {
        RoomResponse roomResponse = new RoomResponse(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("password")
        );
        return roomResponse;
    };

    public int insert(final String name, final String password) {
        final String sql = "insert into room (name, password) values (?, ?)";
        return jdbcTemplate.update(sql, name, password);
    }

    public List<RoomResponse> findAll() {
        final String sql = "select id, name, password from room";
        return jdbcTemplate.query(sql, actorRowMapper);
    }

    public int deleteFrom(String id) {
        final String sql = "delete from room where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public String findPasswordById(String id) {
        final String sql = "select password from room where id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }
}
