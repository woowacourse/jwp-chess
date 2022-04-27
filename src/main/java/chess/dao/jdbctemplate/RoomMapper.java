package chess.dao.jdbctemplate;

import chess.dto.RoomResponseDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class RoomMapper implements RowMapper<RoomResponseDto> {

    @Override
    public RoomResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new RoomResponseDto(rs.getString("id"), rs.getString("name"), rs.getString("pw"));
    }
}
