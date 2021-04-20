package chess.dao;

import chess.dto.CommandDto;
import chess.dto.MoveRequestDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;

@Repository
public class SpringChessLogDao implements ChessRepository {
    private SimpleJdbcInsert simpleJdbcInsert;
    private JdbcTemplate jdbcTemplate;

    public SpringChessLogDao(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("chessGame")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long add(MoveRequestDto moveRequestDto) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(moveRequestDto);
        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public List<CommandDto> find(String roomId) {
        String query = "select target, destination from chessgame where room_id = ? ORDER BY command_date ASC;";
        return jdbcTemplate.query(
                query,
                (resultSet, rowNum) -> {
                    CommandDto commandDto = new CommandDto(
                            roomId,
                            resultSet.getString("target"),
                            resultSet.getString("destination")
                    );
                    return commandDto;
                }, roomId);
    }

    public void delete(String roomId) {
        String query = "DELETE FROM chessgame WHERE room_id = ?";
        this.jdbcTemplate.update(query, roomId);
    }

    public String findRoomByRoomId(String roomId) {
        String query = "select room_id from chessgame where room_id = ?";
        List<String> roomLog = jdbcTemplate.query(
                query,
                (resultSet, rowNum) -> resultSet.getString("room_id")
                , roomId);

        if (Objects.isNull(roomLog)) {
            return null;
        }
        return roomId;
    }
}
