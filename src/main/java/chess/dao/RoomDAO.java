package chess.dao;

import chess.dto.RoomDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDAO {
    private JdbcTemplate jdbcTemplate;

    public RoomDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long createRoom(String roomName) {
        String query = "INSERT INTO room (roomName) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        System.out.println("DAO 내부");
        System.out.println(jdbcTemplate);
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, roomName);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public Optional<Long> findRoomIdByName(String roomName) {
        try {
            String query = "SELECT roomId FROM room WHERE roomName = ? ORDER BY roomId DESC LIMIT 1";
            return jdbcTemplate.queryForObject(
                    query,
                    (resultSet, rowNum) -> {
                        return Optional.ofNullable(resultSet.getLong(1));
                    },
                    roomName);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<RoomDto> findAllRooms() {
        String query = "SELECT * FROM room ORDER BY createdAt DESC";
        return jdbcTemplate.queryForObject(
                query,
                (rs, rowNum) -> {
                    List<RoomDto> rooms = new ArrayList<>();
                    do {
                        rooms.add(new RoomDto(
                                rs.getLong(1),
                                rs.getString(2),
                                rs.getObject(3, LocalDateTime.class)
                        ));
                    } while(rs.next());
                    return rooms;
                });
    }
}
