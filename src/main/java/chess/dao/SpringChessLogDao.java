package chess.dao;

import chess.dto.MoveRequestDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SpringChessLogDao {
    private SimpleJdbcInsert simpleJdbcInsert;
    private JdbcTemplate jdbcTemplate;

    public SpringChessLogDao(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("chessGame")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long addLog(MoveRequestDto moveRequestDto) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(moveRequestDto);
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public List<MoveRequestDto> applyCommand(String roomId) {
        String query = "select target, destination from chessgame where room_id = ? ORDER BY command_date ASC;";
        return jdbcTemplate.query(
                query,
                (resultSet, rowNum) -> {
                    MoveRequestDto moveRequestDto = new MoveRequestDto(
                            roomId,
                            resultSet.getString("target"),
                            resultSet.getString("destination")
                    );
                    return moveRequestDto;
                }, roomId);
    }

    public void deleteLog(String roomId) {
        String query = "DELETE FROM chessgame WHERE room_id = ?";
        this.jdbcTemplate.update(query, roomId);
    }
}
