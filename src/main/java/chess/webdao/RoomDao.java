package chess.webdao;

import chess.webdto.dao.TurnDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * CREATE TABLE room
 * (
 *     room_id    BIGINT      NOT NULL,
 *     turn       VARCHAR(16) NOT NULL,
 *     is_playing BOOLEAN     NOT NULL,
 *     name       VARCHAR(16) NOT NULL,
 *     password   VARCHAR(16),
 *     create_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
 *     PRIMARY KEY (room_id)
 * );
 */
@Repository
public class RoomDao {
    private JdbcTemplate jdbcTemplate;

    public RoomDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<TurnDto> turnMapper = (resultSet, rowNum) -> {
        TurnDto turnDto = new TurnDto();

        turnDto.setTurn(resultSet.getString("turn"));
        turnDto.setIsPlaying(resultSet.getBoolean("is_playing"));

        return turnDto;
    };

    public void deleteRoomByRoomId(long roomId) {
        final String sql = "DELETE FROM room";
        this.jdbcTemplate.update(sql);
    }

    public void changeTurnByRoomId(String turn, boolean isPlaying, long roomId) {
        final String sql = "UPDATE room SET turn = (?), is_playing = (?) WHERE room_id = (?)";
        this.jdbcTemplate.update(sql, turn, isPlaying, roomId);
    }

    public TurnDto selectTurnByRoomId(long roomId) {
        final String sql = "SELECT * FROM room WHERE room_id = (?)";
        return this.jdbcTemplate.queryForObject(sql, turnMapper, roomId);
    }

    public long createRoom(String currentTurn, boolean isPlaying) {
        String sql = "INSERT INTO room (turn, is_playing, name, room_id) VALUES (?, ?, ?, ?)";
        String name = "한글도 되나";
        long roomId = 1;
        return this.jdbcTemplate.update(sql, currentTurn, isPlaying, name, roomId);
    }
}
