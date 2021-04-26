package chess.service.dao;

import chess.dto.RoomDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.ArrayList;
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

    public List<RoomDto> loadRooms() {
        return jdbcTemplate.query("SELECT * FROM room", (rs, rowNum) ->
                new RoomDto(rs.getLong(COLUMN_INDEX_OF_ID), rs.getString(COLUMN_INDEX_OF_NAME)
                ));
    }

    public String name(final long roomId) {
        final String query = "SELECT room_name FROM room WHERE id = ?";
        return jdbcTemplate.queryForObject(query, String.class, roomId);
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

    // TODO :: RoomDTO 생성자로 속성 주입
    public RoomDto roomInfo(final long roomId) {
        final RoomDto roomDto = new RoomDto();
        roomDto.setRoomName(name(roomId));
        roomDto.setPlayer1(player1(roomId));
        roomDto.setPlayer2(player2(roomId));
        return roomDto;
    }

    public List<String> players(final long roomId) {
        final List<String> players = new ArrayList<>();
        addIfExist(players, player1(roomId));
        addIfExist(players, player2(roomId));
        return players;
    }

    private void addIfExist(final List<String> players, final String player) {
        if (Objects.isNull(player)) {
            return;
        }
        players.add(player);
    }
}
