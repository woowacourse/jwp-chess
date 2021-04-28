package chess.dao;

import chess.dto.request.RoomNameRequestDto;
import chess.dto.request.TurnChangeRequestDto;
import chess.dto.response.RoomResponseDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class RoomDao {
    private final JdbcTemplate jdbcTemplate;

    public RoomDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Long addRoom(final RoomNameRequestDto roomNameRequestDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String query = "INSERT INTO room (room_name, current_turn) VALUES (?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"room_id"});
            ps.setString(1, roomNameRequestDto.getRoomName());
            ps.setString(2, "white");
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public List<RoomResponseDto> showAllRooms() {
        List<RoomResponseDto> rooms;
        String query = "SELECT * FROM room";
        rooms = jdbcTemplate.query(
                query,
                (rs, rowName) -> new RoomResponseDto(
                        rs.getLong("room_id"),
                        rs.getString("room_name"),
                        rs.getString("current_turn"))
        );
        return rooms;
    }

    public String showCurrentTurn(final Long roomId) {
        String query = "SELECT current_turn FROM room WHERE room_id=?";
        return jdbcTemplate.queryForObject(query, String.class, roomId);
    }

    public void changeTurn(final TurnChangeRequestDto turnChangeRequestDto) {
        String query = "UPDATE room SET current_turn=? WHERE room_id=?";
        jdbcTemplate.update(query, turnChangeRequestDto.getNextTurn(), turnChangeRequestDto.getRoomId());
    }
}
