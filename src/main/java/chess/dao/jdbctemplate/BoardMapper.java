package chess.dao.jdbctemplate;

import chess.dto.BoardDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class BoardMapper implements RowMapper<BoardDto> {

    @Override
    public BoardDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BoardDto(rs.getString("position"), rs.getString("piece"));
    }
}
