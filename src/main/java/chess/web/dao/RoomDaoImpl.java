package chess.web.dao;

import chess.board.Room;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDaoImpl implements RoomDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Room> rowMapper = (resultSet, rowNum) -> {
        return new Room(
                resultSet.getLong("id"),
                resultSet.getString("turn"),
                resultSet.getString("title"),
                resultSet.getString("password")
        );
    };

    public RoomDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(String turn, String title, String password) {
        String query = "INSERT INTO room (turn, title, password) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(query, new String[]{"id"});
            preparedStatement.setString(1, turn);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, password);
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public Long updateTurnById(Long id, String turn) {
        final String query = "UPDATE room SET turn = ? WHERE id = ?";

        jdbcTemplate.update(query, turn, id);
        return id;
    }

    @Override
    public Optional<Room> findById(Long id) {
        final String query = "SELECT * FROM room WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, rowMapper, id));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public List<Room> findAll() {
        final String query = "SELECT * FROM room";

        return jdbcTemplate.query(query, rowMapper);
    }

    @Override
    public void delete(Long roomId, String password) {
        final String query = "DELETE FROM room WHERE id = ? AND password = ?";
        jdbcTemplate.update(query, roomId, password);
    }
}
