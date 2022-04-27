package chess.application.web.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoomsDao {

    public final JdbcTemplate jdbcTemplate;

    public RoomsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertRoom(final String name, final String password) {
        final String sql = "insert into rooms (id, name, password) values (?, ?, ?)";
        final int roomId = createRoomId(countRooms());
        System.out.println(roomId);
        jdbcTemplate.update(sql, roomId, name, hashedPassword(password));
        clearCommandsInRoom(roomId);
    }

    private void clearCommandsInRoom(final int roomId) {
        final String sql = "delete from command where id = (?)";
        jdbcTemplate.update(sql, roomId);
    }

    private int createRoomId(final int roomId) {
        if (exists(roomId)) {
            return createRoomId(roomId + 1);
        }
        return roomId;
    }

    private int countRooms() {
        final String sql = "select count(*) from rooms";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    private String findNameById(final int id) {
        final String sql = String.format("select name from rooms where id = %d", id);
        return jdbcTemplate.queryForObject(sql, String.class);
    }

    private boolean exists(final int id) {
        final String sql = String.format("select exists(select id from rooms where id = %d)", id);
        return jdbcTemplate.queryForObject(sql, Integer.class) == 1;
    }

    private String hashedPassword(final String password) {
        final String sql = String.format("select md5('%s')", password);
        return jdbcTemplate.queryForObject(sql, String.class);
    }
}
