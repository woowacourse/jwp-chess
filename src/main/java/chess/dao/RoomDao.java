package chess.dao;

import chess.entity.RoomEntity;
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

    private final RowMapper<RoomEntity> actorRowMapper = (resultSet, rowNum) -> {
        RoomEntity roomEntity = new RoomEntity(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("password")
        );
        return roomEntity;
    };

    public int insert(final String name, final String password) {
        final String sql = "insert into room (name, password) values (?, ?)";
        return jdbcTemplate.update(sql, name, password);
    }

    public List<RoomEntity> findAll() {
        final String sql = "select id, name, password from room";
        return jdbcTemplate.query(sql, actorRowMapper);
    }
}
