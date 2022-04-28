package chess.dao;

import chess.dto.RoomDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<RoomDto> roomDtoRowMapper = (resultSet, rowNum) -> new RoomDto(
            resultSet.getString("room_name"),
            resultSet.getString("room_password")
    );

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveRoom(int gameId, RoomDto roomDto) {
        String sql = "insert into room (game_id, room_name, room_password) values (?, ?, ?)";
        jdbcTemplate.update(sql,
                gameId,
                roomDto.getName(),
                roomDto.getPassword());
    }

    public RoomDto findById(int gameId) {
        String sql = "select * from room where game_id=?";
        return jdbcTemplate.queryForObject(sql, roomDtoRowMapper, gameId);
    }

    public void deleteRoom(int gameId) {
        String sql = "delete from room where game_id=?";
        jdbcTemplate.update(sql, gameId);
    }

    public String findPasswordById(int gameId) {
        String sql = "select room_password from room where game_id=?";
        return jdbcTemplate.queryForObject(sql, String.class, gameId);
    }
}
