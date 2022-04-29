package chess.dao;

import chess.entity.Room;
import java.sql.PreparedStatement;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RoomImplDao implements RoomDao {
    
    private final JdbcTemplate jdbcTemplate;

    public RoomImplDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long insertRoom(String title, String password) {
        String query = "INSERT INTO room (state, title, password) VALUES(?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = (connection) -> {
            PreparedStatement prepareStatement = connection.prepareStatement(query, new String[]{"id"});
            prepareStatement.setString(1, "Ready");
            prepareStatement.setString(2, title);
            prepareStatement.setString(3, password);
            return prepareStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public Long updateStateById(Long roomId, String state) {
        String sql = "UPDATE room SET state = (?) WHERE id = (?)";
        jdbcTemplate.update(sql, state, roomId);
        return roomId;
    }

    @Override
    public Room findRoomById(Long roomId) {
        String sql = "SELECT * FROM room WHERE id = (?)";
        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> new Room(
                        rs.getLong("id"),
                        rs.getString("state"),
                        rs.getString("title"),
                        rs.getString("password")),
                roomId);
    }
}
