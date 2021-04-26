package chess.repository.spring;

import chess.domain.room.Room;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class RoomDAO {
    private static final RowMapper<Room> ROW_MAPPER = (resultSet, rowNumber) -> {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        return new Room(id, name);
    };

    private final JdbcTemplate jdbcTemplate;

    public RoomDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Room> findAllRooms() {
        String query = "SELECT * FROM ROOM";
        return jdbcTemplate.query(query, ROW_MAPPER);
    }

    public int insertRoom(String name) {
        String query = "INSERT INTO ROOM (NAME) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = (connection) -> {
            PreparedStatement prepareStatement = connection.prepareStatement(query, new String[]{"id"});
            prepareStatement.setString(1, name);
            return prepareStatement;
        };
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return (int) keyHolder.getKey();
    }

    public Optional<Room> findLastAddedRoom() {
        String query = "SELECT * FROM ROOM ORDER BY ID DESC LIMIT 1";
        List<Room> rooms = jdbcTemplate.query(query, ROW_MAPPER);
        if (rooms.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(rooms.get(0));
    }

    public void deleteRoomById(int id) {
        String query = "DELETE FROM ROOM WHERE ID = ?";
        jdbcTemplate.update(query, id);
    }
}
