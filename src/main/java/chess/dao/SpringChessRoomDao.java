package chess.dao;

import chess.dto.RoomDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SpringChessRoomDao implements ChessRoomRepository {
    private SimpleJdbcInsert simpleJdbcInsert;
    private JdbcTemplate jdbcTemplate;

    public SpringChessRoomDao(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("chessRoom")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long add(RoomDto roomDto) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(roomDto);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }

    @Override
    public List<RoomDto> findAllRoom() {
        String query = "select * from chessroom order by room_id asc";
        return jdbcTemplate.query(
                query,
                (resultSet, rowNum) -> {
                    RoomDto roomDto = new RoomDto(
                            resultSet.getString("room_id"),
                            resultSet.getString("room_name"),
                            resultSet.getString("room_password")
                    );
                    return roomDto;
                }
        );
    }

    @Override
    public String findRoomNameById(String id) {
        String query = "select target, destination from chessroom where room_id = ? ORDER BY command_date ASC;";
        return jdbcTemplate.queryForObject(query, String.class, id);
    }

    @Override
    public void delete(String id) {
        String query = "DELETE FROM chessroom WHERE room_id = ?";
        this.jdbcTemplate.update(query, id);
    }
}
