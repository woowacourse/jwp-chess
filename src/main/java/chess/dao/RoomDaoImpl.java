package chess.dao;

import chess.entity.Room;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDaoImpl implements RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Room room) {
        String sql = "insert into room (turn, name) values (?, ?)";
        jdbcTemplate.update(sql, room.getTurn(), room.getName());
    }

    @Override
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

    @Override
    public void update(long id, String turn) {
        String sql = "update room set turn = ? where id = ?";
        jdbcTemplate.update(sql, turn, id);
    }


}
