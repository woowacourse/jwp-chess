package chess.dao;

import chess.dto.RoomDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomDao {
    private final JdbcTemplate jdbcTemplate;

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RoomDto> loadRoomList() {
        String query = "SELECT game_id, name FROM room";
        return this.jdbcTemplate.query(query, (res, rowNum) ->
                new RoomDto(res.getInt("game_id"), res.getString("name")));
    }

    public int countRoomByName(String roomName) {
        String query = "SELECT count(*) FROM room WHERE name = ?";
        return this.jdbcTemplate.queryForObject(query, Integer.class, roomName);
    }

    public void saveRoom(int gameId, String roomName) {
        String query = "INSERT INTO room(game_id, name) VALUES(?, ?)";
        this.jdbcTemplate.update(query, gameId, roomName);
    }

    public String loadRoomName(int gameId) {
        String query = "SELECT name FROM room WHERE game_id = ?";
        return this.jdbcTemplate.queryForObject(query, String.class, gameId);
    }
}
