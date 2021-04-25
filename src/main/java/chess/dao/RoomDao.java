package chess.dao;

import chess.dto.request.RoomRequestDto;
import chess.dto.response.RoomResponseDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class RoomDao {
    private final JdbcTemplate jdbcTemplate;

    public RoomDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void addRoom(final RoomRequestDto roomRequestDto) {
        String query = "INSERT INTO room (room_name) VALUES (?)";
        jdbcTemplate.update(query, roomRequestDto.getRoomName());
    }

    public List<RoomResponseDto> showAllRooms() {
        List<RoomResponseDto> rooms;
        String query = "SELECT * FROM room";
        rooms = jdbcTemplate.query(
                query, (rs, rowName) -> new RoomResponseDto(
                        rs.getLong("room_id"),
                        rs.getString("room_name"))
        );
        return rooms;
    }
}
