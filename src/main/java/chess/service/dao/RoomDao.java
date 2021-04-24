package chess.service.dao;

import chess.controller.dto.RoomInfoDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class RoomDao {
    private static final int COLUMN_INDEX_OF_ID = 1;
    private static final int COLUMN_INDEX_OF_NAME = 2;

    private final JdbcTemplate jdbcTemplate;
    private final KeyHolder keyHolder;

    public RoomDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.keyHolder = new GeneratedKeyHolder();
    }

    public long save(final String roomName, final String player1) {
        if (isRoomNameExist(roomName)) {
            throw new IllegalArgumentException("중복된 방 이름입니다.");
        }

        final String query = "INSERT INTO room (room_name, player1) VALUES (?, ?)";
        jdbcTemplate.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(query, new String[]{"id"});
            pstmt.setString(1, roomName);
            pstmt.setString(2, player1);
            return pstmt;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    private boolean isRoomNameExist(final String roomName) {
        final String query = "SELECT EXISTS (SELECT * FROM room WHERE room_name = ?)";
        return jdbcTemplate.queryForObject(query, Boolean.class, roomName);
    }

    public void delete(final long roomId) {
        jdbcTemplate.update("DELETE FROM room WHERE id = ?", roomId);
    }

    public List<RoomInfoDto> load() {
        return jdbcTemplate.query( "SELECT * FROM room", (rs, rowNum) ->
                new RoomInfoDto(rs.getLong(COLUMN_INDEX_OF_ID), rs.getString(COLUMN_INDEX_OF_NAME)
        ));
    }

    public String name(final long roomId) {
        final String query = "SELECT room_name FROM room WHERE id = ?";
        return jdbcTemplate.queryForObject(query, String.class, roomId);
    }

    public boolean isJoined(final long roomId, final String player){
        final String player1 = player1(roomId);
        final String player2 = player2(roomId);

        final boolean isPlayer1 = !Objects.isNull(player1) && player1.equals(player);
        final boolean isPlayer2 = !Objects.isNull(player2) && player2.equals(player);

        return isPlayer1 || isPlayer2;
    }

    public boolean isFull(long roomId) {
        final String player1 = player1(roomId);
        final String player2 = player2(roomId);
        return !Objects.isNull(player1) && !Objects.isNull(player2);
    }

    public void enter(final long roomId, final String playerId) {
        final String query = "UPDATE room SET player2 = ? WHERE id = ?";
        jdbcTemplate.update(query, playerId, roomId);
    }

    private String player1(final long roomId) {
        final String query = "SELECT player1 FROM room WHERE id = ?";
        return jdbcTemplate.queryForObject(query, String.class, roomId);
    }

    private String player2(final long roomId) {
        final String query = "SELECT player2 FROM room WHERE id = ?";
        return jdbcTemplate.queryForObject(query, String.class, roomId);
    }
}
