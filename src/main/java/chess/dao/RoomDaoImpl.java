package chess.dao;

import java.util.List;
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
        String sql = "insert into room (name, password, turn) values (?, ?, ?)";
        jdbcTemplate.update(sql, room.getName(), room.getPassword(), room.getTurn());
    }

    @Override
    public Optional<Room> findById(long roomId) {
        String sql = "select * from room where id = ?";

        try {
            Room room = jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new Room(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("turn")
                ), roomId);
            return Optional.ofNullable(room);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Room> findByName(String name) {
        String sql = "select * from room where name = ?";

        try {
            Room room = jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new Room(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("turn")
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
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("turn")
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

    @Override
    public List<Room> findAll() {
        String sql = "select id, name, turn from room";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Room(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("turn")
        ));
    }

    @Override
    public Optional<Room> findByIdAndPassword(long id, String password) {
        String sql = "select * from room where id = ? AND password = ?";

        try {
            Room room = jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new Room(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("turn")
                ), id, password);
            return Optional.ofNullable(room);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(long roomId) {
        String sql = "delete from room where id = ?";
        jdbcTemplate.update(sql, roomId);
    }
}
