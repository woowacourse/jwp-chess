package chess.dao;

import chess.dto.GameDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class GameMapper implements RowMapper<GameDto> {
    @Override
    public GameDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new GameDto(rs.getInt("id"), rs.getString("title"));
    }
}
