package chess.dao;

import chess.dto.RoomDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class SpringChessRoomDao {
    private SimpleJdbcInsert simpleJdbcInsert;
    private JdbcTemplate jdbcTemplate;

    public SpringChessRoomDao(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("chessRoom")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long addRoom(RoomDto roomDto) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(roomDto);
        return simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
    }

    public String findRoomNameById(String id) {
        String query = "select target, destination from chessroom where room_id = ? ORDER BY command_date ASC;";
        return jdbcTemplate.queryForObject(query, String.class, id);
    }

    public void delete(String id) {
        String query = "DELETE FROM chessroom WHERE room_id = ?";
        this.jdbcTemplate.update(query, id);
    }
}
