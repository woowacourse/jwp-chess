package chess.service.dao;

import chess.dto.RoomDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

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

    public void delete(final Long roomId) {
        jdbcTemplate.update("DELETE FROM room WHERE id = ?", roomId);
    }

    public List<RoomDto> loadRooms() {
        return jdbcTemplate.query("SELECT * FROM room", (rs, rowNum) ->
                new RoomDto(rs.getLong(COLUMN_INDEX_OF_ID), rs.getString(COLUMN_INDEX_OF_NAME)
                ));
    }

    public String name(final Long roomId) {
        final String query = "SELECT room_name FROM room WHERE id = ?";
        return jdbcTemplate.queryForObject(query, String.class, roomId);
    }
}
