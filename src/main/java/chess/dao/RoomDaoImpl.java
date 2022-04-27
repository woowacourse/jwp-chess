package chess.dao;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.entity.Room;

@Repository
public class RoomDaoImpl implements RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Room room) {
        String sql = "insert into room (turn, name, password) values (?, ?, ?)";
        jdbcTemplate.update(sql, room.getTurn(), room.getName(), room.getPassword());
    }

    @Override
    public Optional<Room> findByName(String name) {
        String sql = "select * from room where name = ?";

        try {
            Room room = jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new Room(
                    rs.getLong("id"),
                    rs.getString("password"),
                    rs.getString("turn"),
                    rs.getString("name")
                ), name);
            return Optional.ofNullable(room);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Room> findByNameAndPassword(String name, String password) {
        String sql = "select * from room where name = ? AND password = ?";

        try {
            Room room = jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new Room(
                    rs.getLong("id"),
                    rs.getString("password"),
                    rs.getString("turn"),
                    rs.getString("name")
                ), name, password);
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
