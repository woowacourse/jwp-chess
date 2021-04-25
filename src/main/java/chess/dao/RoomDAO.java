package chess.dao;

import chess.dto.room.RoomDTO;
import chess.exception.InitialSettingDataException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class RoomDAO {
    private static final int DEFAULT_BLACK_USER_ID = 1;
    private static final int DEFAULT_WHITE_USER_ID = 2;
    private static final int PLAYING_STATUS = 1;
    private static final int READY_STATUS = 2;

    private final JdbcTemplate jdbcTemplate;

    public RoomDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long createRoom(final String name, final int whiteUserId) {
        String query = "INSERT INTO room (title, white_user, status) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(query, new String[]{"id"});
            ps.setString(1, name);
            ps.setInt(2, whiteUserId);
            ps.setInt(3, READY_STATUS);
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<RoomDTO> allRooms() {
        try {
            String query = "SELECT room.id, room.title, black.nickname AS black_user, white.nickname AS white_user, room.status " +
                    "FROM room LEFT JOIN player as black on black.id = room.black_user " +
                    "LEFT JOIN player as white on white.id = room.white_user ORDER BY room.status DESC, room.id DESC";

            return jdbcTemplate.query(query, mapper());
        } catch (DataAccessException e) {
            throw new InitialSettingDataException();
        }
    }

    private RowMapper<RoomDTO> mapper() {
        return (resultSet, rowNum) -> {
            int status = resultSet.getInt("status");
            boolean playing = (status == 1);
            return new RoomDTO(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getString("black_user"),
                    resultSet.getString("white_user"),
                    status,
                    playing
            );
        };
    }

    public void changeStatusEndByRoomId(final String roomId) {
        String query = "UPDATE room SET status = 0 WHERE id = ?";
        jdbcTemplate.update(query, roomId);
    }

    public List<String> allRoomIds() {
        try {
            RowMapper<String> rowMapper = (resultSet, rowNum) -> resultSet.getString("id");

            String query = "SELECT id FROM room";
            return jdbcTemplate.query(query, rowMapper);
        } catch (DataAccessException e) {
            throw new InitialSettingDataException();
        }
    }

    public void joinBlackUser(final String roomId, final int blackUserId) {
        String query = "UPDATE room SET black_user = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(query, blackUserId, PLAYING_STATUS, roomId);
    }

    public String findBlackUserById(final String id) {
        return jdbcTemplate.queryForObject("SELECT black_user FROM room WHERE id = ?",
                String.class,
                id);
    }

    public String findWhiteUserById(final String id) {
        return jdbcTemplate.queryForObject("SELECT white_user FROM room WHERE id = ?",
                String.class,
                id);
    }
}
