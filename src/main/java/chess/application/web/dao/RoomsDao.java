package chess.application.web.dao;

import chess.view.Room;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class RoomsDao {

    public static final int EXISTS_ROOM = 1;
    public static final int INITIAL_ROOM_ID = 0;
    public static final int ADD_ROOM_ID = 1;
    private final JdbcTemplate jdbcTemplate;

    public RoomsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertRoom(final int roomId, final String name, final String password) {
        final String sql = "insert into rooms (id, name, password) values (?, ?, ?)";
        jdbcTemplate.update(sql, roomId, name, hashedPassword(password));
        clearCommandsInRoom(roomId);
    }

    private void clearCommandsInRoom(final int roomId) {
        final String sql = "delete from command where id = (?)";
        jdbcTemplate.update(sql, roomId);
    }

    public int createRoomId() {
        if (countRooms() >= EXISTS_ROOM) {
            final String sql = "select id from rooms order by id desc limit 1";
            return jdbcTemplate.queryForObject(sql, Integer.class) + ADD_ROOM_ID;
        }
        return INITIAL_ROOM_ID;
    }

    public int countRooms() {
        final String sql = "select count(*) from rooms";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public String findNameById(final int id) {
        final String sql = String.format("select name from rooms where id = %d", id);
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    private String hashedPassword(final String password) {
        final String sql = String.format("select md5('%s')", password);
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    public List<Room> findAll() {
        final String sql = "select id, name from rooms";
        return jdbcTemplate.query(sql,
                ((rs, rowNum) -> new Room(rs.getInt("id"), rs.getString("name"))));
    }

    public void removeRoom(final int roomId, final String password) {
        final String sql = String.format("delete from rooms where id=(?) and password=md5('%s')", password);
        jdbcTemplate.update(sql, roomId);
    }
}
