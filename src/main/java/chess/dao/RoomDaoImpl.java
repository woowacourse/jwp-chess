package chess.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import chess.dto.RoomDto;
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
    public Optional<Room> findById(Long roomId) {
        String sql = "select * from room where id = ?";

        try {
            Room room = jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new Room(
                    rs.getLong("id"),
                    rs.getString("password"),
                    rs.getString("turn"),
                    rs.getString("name")
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
    public void update(Long id, String turn) {
        String sql = "update room set turn = ? where id = ?";
        jdbcTemplate.update(sql, turn, id);
    }

    @Override
    public List<RoomDto> findAll() {
        String sql = "select id, turn, name from room";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new RoomDto(
            rs.getLong("id"),
            rs.getString("turn"),
            rs.getString("name")
        ));
    }

    @Override
    public Optional<Room> findByIdAndPassword(Long id, String password) {
        String sql = "select * from room where id = ? AND password = ?";

        try {
            Room room = jdbcTemplate.queryForObject(sql,
                (rs, rowNum) -> new Room(
                    rs.getLong("id"),
                    rs.getString("password"),
                    rs.getString("turn"),
                    rs.getString("name")
                ), id, password);
            return Optional.ofNullable(room);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Long roomId) {
        String sql = "delete from room where id = ?";
        jdbcTemplate.update(sql, roomId);
    }
}
