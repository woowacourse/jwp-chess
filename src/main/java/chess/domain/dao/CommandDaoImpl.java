package chess.domain.dao;

import chess.domain.dto.CommandDto;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CommandDaoImpl implements CommandDao {
    private final JdbcTemplate jdbcTemplate;

    public CommandDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insert(CommandDto commandDto, int id) {
        String query = "INSERT INTO Command (data, history_id) VALUES (?, ?)";
        jdbcTemplate.update(query, commandDto.data(), id);
    }

    @Override
    public List<CommandDto> selectAllCommands(String id) {
        String query = "SELECT * FROM History H JOIN Command C on H.history_id = C.history_id"
            + " WHERE H.history_id = ? AND H.is_end = false";
        return jdbcTemplate.query(query,
            (rs, rowNum) -> {
                return new CommandDto(rs.getString("data"));
            },
            id);
    }
}
