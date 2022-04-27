package chess.web.dao;

import chess.board.Room;
import java.sql.PreparedStatement;
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
                resultSet.getInt("board_id"),
                resultSet.getString("title"),
                resultSet.getString("password")
        );
    };

    public RoomDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Long boardId, String title, String password) {
        String query = "INSERT INTO room (board_id, title, password) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(query, new String[]{"id"});
            preparedStatement.setLong(1, boardId);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, password);
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public Optional<Room> findByBoardId(Long boardId) {
        final String query = "SELECT * FROM room WHERE board_id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, rowMapper, boardId));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Long boardId, String password) {
        final String query = "DELETE FROM room WHERE board_id = ? AND password = ?";
        jdbcTemplate.update(query, boardId, password);
    }
}
