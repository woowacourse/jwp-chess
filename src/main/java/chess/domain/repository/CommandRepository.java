package chess.domain.repository;

import chess.domain.dto.CommandDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommandRepository {
    private final JdbcTemplate jdbcTemplate;

    public CommandRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(CommandDto commandDto, int id) {
        String query = "INSERT INTO Command (data, history_id) VALUES (?, ?)";
        jdbcTemplate.update(query, commandDto.data(), id);
    }

    public List<CommandDto> selectAllCommands(String id) {
        String query = "SELECT * FROM history H JOIN Command C on H.history_id = C.history_id WHERE H.history_id = ? AND H.is_end = false";
        return jdbcTemplate.query(query,
                (rs, rowNum) -> {
                    CommandDto commandDto = new CommandDto(rs.getString("data"));
                    return commandDto;
                },
                id);
    }
}
