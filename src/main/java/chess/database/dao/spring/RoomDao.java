package chess.database.dao.spring;

import chess.database.dto.RoomDto;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
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
        try {
            return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> {
                int id = resultSet.getInt("id");
                String password = resultSet.getString("password");
                return new RoomDto(id, roomName, password);
            }, roomName);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public RoomDto findById(int roomId) {
        String sql = "select * from room where id = ?";
        return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> {
            String roomName = resultSet.getString("name");
            String password = resultSet.getString("password");
            return new RoomDto(roomId, roomName, password);
        }, roomId);
    }

    public void delete(RoomDto roomDto) {
        final String sql = "DELETE FROM room WHERE name = ? and password = ?";
        jdbcTemplate.update(sql, roomDto.getName(), roomDto.getPassword());
    }

    public void delete(int roomId) {
        final String sql = "DELETE FROM room WHERE id = ?";
        jdbcTemplate.update(sql, roomId);
    }

    public List<RoomDto> findAll() {
        String sql = "select id, name from room";
        return jdbcTemplate.query(sql, (resultSet, rowNum) ->
            new RoomDto(resultSet.getInt("id"), resultSet.getString("name"))
        );
    }
}
