package chess.repository;

import chess.dto.RoomDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class RoomRepository {
    private final JdbcTemplate jdbcTemplate;

    public RoomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<RoomDto> rowMapper = (resultSet, rowNum) ->
            new RoomDto(resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getBoolean("is_full"));

    public int insert(String name) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final String query = "INSERT INTO Room (name) VALUES (?)";
        jdbcTemplate.update((Connection con) -> {
            PreparedStatement pstmt = con.prepareStatement(
                    query,
                    new String[]{"id"});
            pstmt.setString(1, name);
            return pstmt;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    public Optional<Integer> findIdByName(String name) {
        final String query = "SELECT id FROM Room WHERE name = ? AND is_full = false " +
                "ORDER BY id DESC limit 1";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, Integer.class, name));
    }

    public List<RoomDto> selectActiveRooms() {
        final String query = "SELECT * FROM Room WHERE is_end = false";
        return jdbcTemplate.query(query, rowMapper);
    }

    public void updateToFull(String id) {
        final String query = "UPDATE Room SET is_full = true WHERE id = ?";
        jdbcTemplate.update(query, id);
    }

    public boolean checkRoomIsFull(String id) {
        final String query = "SELECT is_full FROM Room WHERE id = ?";
        return jdbcTemplate.queryForObject(query, Boolean.class, id);
    }

    public void updateToEnd(String id) {
        final String query = "UPDATE Room SET is_end = true WHERE id = ?";
        jdbcTemplate.update(query, id);
    }

    public boolean isEnd(String id) {
        final String query = "SELECT is_end FROM Room WHERE id = ?";
        return jdbcTemplate.queryForObject(query, Boolean.class, id);
    }
}
