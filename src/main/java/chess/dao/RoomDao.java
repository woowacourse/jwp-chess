package chess.dao;

import chess.dto.RoomDto;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {
    private static final int TRASH_ID = -1;

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<RoomDto> roomDtoRowMapper = (resultSet, rowNum) -> new RoomDto(
            resultSet.getInt("game_id"),
            resultSet.getString("room_name"),
            resultSet.getString("room_password"),
            resultSet.getString("status")
    );

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveRoom(RoomDto roomDto) {
        String sql = "insert into room (game_id, room_name, room_password, status) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                roomDto.getId(),
                roomDto.getName(),
                roomDto.getPassword(),
                roomDto.getStatus());
    }

    /*public RoomDto findById(int gameId) {
        String sql = "select * from room where game_id=?";
        return jdbcTemplate.queryForObject(sql, roomDtoRowMapper, gameId);
    }*/

    public List<RoomDto> findAll() {
        String sql = "select * from room";
        return jdbcTemplate.query(sql, roomDtoRowMapper);
    }

    public void deleteRoom(int gameId) {
        String sql = "delete from room where game_id=?";
        jdbcTemplate.update(sql, gameId);
    }

    public String findPasswordById(int gameId) {
        String sql = "select room_password from room where game_id=?";
        return jdbcTemplate.queryForObject(sql, String.class, gameId);
    }

    public String findStatusById(int gameId) {
        String sql = "select status from room where game_id=?";
        return jdbcTemplate.queryForObject(sql, String.class, gameId);
    }

    public void updateStatus(int gameId, String status) {
        String sql = "update room set status=? where game_id=?";
        jdbcTemplate.update(sql, status, gameId);
    }
}
