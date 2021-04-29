package chess.dao;

import chess.dto.MoveRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class MoveRequestMapper implements RowMapper<MoveRequest> {

    private static final String SOURCE_POSITION = "start";
    private static final String TARGET_POSITION = "end";

    @Override
    public MoveRequest mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        String from = resultSet.getString(SOURCE_POSITION);
        String to = resultSet.getString(TARGET_POSITION);
        return new MoveRequest(from, to);
    }
}
