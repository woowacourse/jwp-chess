package chess.dao;

import chess.entity.Room;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Room room) {
        String sql = "insert into room (turn, name) values (?, ?)";
        jdbcTemplate.update(sql, room.getTurn(), room.getName());
    }

    public Optional<Room> findByName(String name) {
        String sql = "select * from room where name = ?";

        try {
            Room room = jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) -> {
                        return new Room(
                                rs.getLong("id"),
                                rs.getString("turn"),
                                rs.getString("name")
                        );
                    }, name);
            return Optional.ofNullable(room);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<Room> findById(long id) {
        String sql = "select * from room r where id = ?";

        return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> {
                    return new Room(
                            rs.getLong("id"),
                            rs.getString("turn"),
                            rs.getString("name")
                    );
                }, id));
    }

    public void update(long id, String turn) {
        String sql = "update room set turn = ? where id = ?";
        jdbcTemplate.update(sql, turn, id);
    }


}
