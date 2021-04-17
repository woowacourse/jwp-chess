package chess.dao.spring;

import chess.dto.web.RoomDto;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {

    private JdbcTemplate jdbcTemplate;

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String insert(RoomDto roomDto) {
        String query = "INSERT INTO rooms (name, is_opened, white, black) VALUES(?, true, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection
                .prepareStatement(query, new String[]{"id"});
            preparedStatement.setString(1, roomDto.getName());
            preparedStatement.setString(2, roomDto.getWhite());
            preparedStatement.setString(3, roomDto.getBlack());
            return preparedStatement;
        }, keyHolder);

        return String.valueOf(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    public List<RoomDto> openedRooms() {
        String query = "SELECT id, name, white, black FROM rooms WHERE is_opened = true";
        return jdbcTemplate.query(
            query,
            (resultSet, rowNum) -> new RoomDto(
                resultSet.getString(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4)
            ));
    }

    public void close(String roomId) {
        String query = "UPDATE rooms SET is_opened = false WHERE id = (?)";
        jdbcTemplate.update(query, roomId);
    }
}
