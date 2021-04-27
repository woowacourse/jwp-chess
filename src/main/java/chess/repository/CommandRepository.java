package chess.repository;

import chess.dto.CommandDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommandRepository {
    private final JdbcTemplate jdbcTemplate;

    public CommandRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(CommandDto commandDto, String id) {
        String query = "INSERT INTO Command (data, room_id) VALUES (?, ?)";
        jdbcTemplate.update(query, commandDto.data(), id);
    }

    public List<CommandDto> selectAllCommandsByRoomId(String roomId) {
        String query = "SELECT * FROM Command WHERE room_id = ?";
        return jdbcTemplate.query(query,
                (rs, rowNum) -> {
                    CommandDto commandDto = new CommandDto(rs.getString("data"));
                    return commandDto;
                },
                roomId);
    }
}
