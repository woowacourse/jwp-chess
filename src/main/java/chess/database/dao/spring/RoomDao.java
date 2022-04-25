package chess.database.dao.spring;

import chess.database.dto.RoomDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

public class RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public RoomDto create(RoomDto roomDto) {
        String sql = "insert into room(name, password) values (?, ?)";
        jdbcTemplate.update(sql, roomDto.getName(), roomDto.getPassword());
        return findByName(roomDto.getName());
    }

    public RoomDto findByName(String roomName) {
        String sql = "select * from room where name = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> {
            int id = resultSet.getInt("id");
            String password = resultSet.getString("password");
            return new RoomDto(id, roomName, password);
        }, roomName);
    }

    public void delete(RoomDto roomDto) {
        final String sql = "DELETE FROM room WHERE name = ? and password = ?";
        jdbcTemplate.update(sql, roomDto.getName(), roomDto.getPassword());
    }

    public List<RoomDto> findAll() {
        String sql = "select id, name from room";
        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) ->
            List.of(new RoomDto(resultSet.getInt("id"), resultSet.getString("name")))
        );
    }
}
