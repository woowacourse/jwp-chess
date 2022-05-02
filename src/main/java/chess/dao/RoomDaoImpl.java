package chess.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import chess.entity.Room;

@Repository
public class RoomDaoImpl implements RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long save(Room room) {
        String sql = "insert into room (name, password, turn) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
            ps.setString(1, room.getName());
            ps.setString(2, room.getPassword());
            ps.setString(3, room.getTurn());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public Optional<Room> findById(long roomId) {
        String sql = "select * from room where id = ?";

        try {
            Room room = jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> getCompleteRoom(rs), roomId);
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
                (rs, rowNum) -> getCompleteRoom(rs), name);
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
                (rs, rowNum) -> getCompleteRoom(rs), name, password);
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
                (rs, rowNum) -> getCompleteRoom(rs), id, password);
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

    private Room getCompleteRoom(ResultSet rs) throws SQLException {
        return new Room(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("password"),
            rs.getString("turn"));
    }
}
