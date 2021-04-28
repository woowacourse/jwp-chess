package chess.dao;

import chess.controller.spring.params.Page;
import chess.dto.RoomDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RoomDAO {
    private static final int MAX_ROWS = 20;
    private static final int GAP_BETWEEN_PAGE_AND_OFFSET = 1;
    private static final int FIRST_PAGE = 1;
    private final JdbcTemplate jdbcTemplate;

    public RoomDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long createRoom(String roomName) {
        String query = "INSERT INTO room (roomName) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
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
        return findAllRooms(new Page(FIRST_PAGE));
    }

    public List<RoomDto> findAllRooms(Page page) {
        String query = "SELECT * FROM room ORDER BY createdAt DESC LIMIT ? OFFSET ?";
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
                }, page.getPageSize(), page.getOffset());
    }
}
