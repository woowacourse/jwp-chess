package chess.dao;

import chess.dto.MoveRequestDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SpringChessLogDao {
    private static final String DELIMITER = ",";
    private SimpleJdbcInsert simpleJdbcInsert;
    private JdbcTemplate jdbcTemplate;

    public SpringChessLogDao(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("chessGame")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLog(String roomId, String target, String destination) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("room_id", roomId);
        parameters.put("target", target);
        parameters.put("destination", destination);
        simpleJdbcInsert.execute(parameters);
    }

    public List<String> applyCommand(String roomId) {
        String query = "select target, destination from chessgame where room_id = ? ORDER BY command_date ASC;";
        return jdbcTemplate.query(
                query,
                (resultSet, rowNum) -> resultSet.getString("target") + DELIMITER +
                            resultSet.getString("destination")
                , roomId);
    }

    public void deleteLog(String roomId) {
        String query = "DELETE FROM chessgame WHERE room_id = ?";
        this.jdbcTemplate.update(query, roomId);
    }
}
