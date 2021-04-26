package chess.dao;

import chess.domain.Room;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {

    private final JdbcTemplate jdbcTemplate;
    private final RoomRequestMapper mapper;

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = new RoomRequestMapper();
    }

    public List<Room> getRooms() {
        String query = "select * from room order by room.id desc";
        return jdbcTemplate.query(query, mapper);
    }

    public void delete(int id) {
        String query = "delete from room where id = ?";
        jdbcTemplate.update(query, id);
    }

    public void insert(String name) {
        String query = "insert into room (name) values ?";
        jdbcTemplate.update(query, name);
    }

    public void deleteAll() {
        String query = "delete from room";
        jdbcTemplate.update(query);
    }
}
