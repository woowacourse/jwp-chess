package chess.dao;

import chess.exception.NoDataExistenceException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ChessDAO {

    private final JdbcTemplate jdbcTemplate;

    public ChessDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Room> allRooms() {
        String query = "SELECT * FROM room inner join chess using (game_id)";

        List<Room> room = jdbcTemplate.query(query, (rs, i) -> {
            Long roomId = rs.getLong("room_id");
            String roomName = rs.getString("room_name");
            Long gameId = rs.getLong("game_id");
            String data = rs.getString("data");

            return new Room(roomId, roomName, gameId, data);
        });

        return room;
    }

    public Long addChessGame(String data) {
        String query = "INSERT INTO chess (data) VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(query, new String[]{"game_id"});
            pstmt.setString(1, data);

            return pstmt;
        }, keyHolder);

        return keyHolder.getKeyAs(Long.class);
    }

    public void updateChessGame(Long gameId, String data) {
        String query = "UPDATE chess SET data=? WHERE game_id = ?";

        jdbcTemplate.update(query, data, gameId);
    }

    public Room findRoomByRoomId(Long gameId) {
        String query = "SELECT `room_id`, `room_name`, `data` FROM room a inner join chess b on (a.game_id = b.game_id) WHERE a.game_id = ?";

        List<Room> room = jdbcTemplate.query(query, (rs, i) -> {
            Long roomId = rs.getLong("room_id");
            String roomName = rs.getString("room_name");
            String data = rs.getString("data");

            return new Room(roomId, roomName, gameId, data);
        }, gameId);

        if (room.size() == 0) {
            throw new NoDataExistenceException();
        }

        return room.get(0);
    }

    public Long addRoom(String roomName, Long gameId) {
        String query = "INSERT INTO room (room_name, game_id) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(query, new String[]{"room_id"});
            pstmt.setString(1, roomName);
            pstmt.setLong(2, gameId);

            return pstmt;
        }, keyHolder);

        return keyHolder.getKeyAs(Long.class);
    }

    public void updateRoom(Long roomId, String roomName, Long gameId) {
        String query = "UPDATE SET room_name=?, game_id=? where room_id = ?";

        jdbcTemplate.update(query, roomName, gameId, roomId);
    }

    public static class Room {

        private final Long roomId;
        private final String roomName;
        private final Long gameId;
        private final String gameData;

        public Room(Long roomId, String roomName, Long gameId, String gameData) {
            this.roomId = roomId;
            this.roomName = roomName;
            this.gameId = gameId;
            this.gameData = gameData;
        }

        public Room(Long roomId, String roomName) {
            this(roomId, roomName, null, null);
        }

        public Long getRoomId() {
            return roomId;
        }

        public String getRoomName() {
            return roomName;
        }

        public Long getGameId() {
            return gameId;
        }

        public String getGameData() {
            return gameData;
        }

    }

}
