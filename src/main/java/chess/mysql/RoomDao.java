package chess.mysql;

import chess.chessgame.domain.room.Room;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;

@Repository
public class RoomDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<RoomDto> rowMapper = (rs, rownum) -> {
        long roomId = rs.getLong("room_id");
        long gameId = rs.getLong("game_id");
        String roomName = rs.getString("room_name");
        long whiteUserId = rs.getLong("user1");
        long blackUserId = rs.getLong("user2");

        return new RoomDto(roomId, roomName, gameId, blackUserId, whiteUserId);
    };

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Room insertRoom(Room entity) {
        String query =
                "INSERT INTO chess.room (game_id, room_name, user1) VALUES " +
                        "(?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection ->
                {
                    PreparedStatement ps = connection.prepareStatement(query, new String[]{"room_id"});
                    ps.setLong(1, entity.getGameManager().getId());
                    ps.setString(2, entity.getRoomName());
                    ps.setLong(3, entity.getWhiteUser().getUserId());
                    return ps;
                }
                , keyHolder
        );

        return new Room(keyHolder.getKey().longValue(), entity.getRoomName(),
                entity.getGameManager(), Arrays.asList(entity.getWhiteUser()));
    }

    public List<RoomDto> findAllActiveRoom() {
        String query =
                "SELECT * " +
                        "FROM chess.room r " +
                        "WHERE game_id IN (" +
                        " SELECT id FROM chess.chessgame" +
                        " WHERE running = true" +
                        ") ";

        return jdbcTemplate.query(query, rowMapper);
    }

    public RoomDto findByRoomId(long roomId) {
        String query =
                "SELECT * " +
                        "FROM chess.room " +
                        "WHERE room_id = ?";

        return jdbcTemplate.query(query, rowMapper, roomId).stream().findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 방이 없습니다."));
    }

    public RoomDto findByUserId(long userId) {
        String query =
                "SELECT * " +
                        "FROM chess.room " +
                        "WHERE  user1 = ? OR user2 = ?";

        return jdbcTemplate.query(query, rowMapper, userId, userId).stream().findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 방이 없습니다."));
    }

    public void updateBlackUser(Room entity) {
        String query =
                "UPDATE chess.room " +
                        "SET user2 = ? " +
                        "WHERE room_id = ?";

        jdbcTemplate.update(query, entity.getBlackUser().getUserId(), entity.getRoomId());
    }

    public void deleteRoom(Room entity) {
        String query =
                "DELETE FROM chess.room WHERE room_id = ?";

        jdbcTemplate.update(query, entity.getRoomId());
    }
}
