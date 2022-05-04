package chess.dao;

import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.entity.RoomEntity;
import chess.exception.NotFoundException;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDao {

    private final JdbcTemplate jdbcTemplate;

    public RoomDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public RoomEntity findById(final int roomId) {
        try {
            final String sql = "SELECT * FROM room WHERE room_id = ?";
            final RowMapper<RoomEntity> rowMapper = createRoomEntityRowMapper();
            return jdbcTemplate.queryForObject(sql, rowMapper, roomId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("방이 존재하지 않습니다.");
        }
    }

    private RowMapper<RoomEntity> createRoomEntityRowMapper() {
        return (resultSet, rowNum) -> new RoomEntity(
                Integer.parseInt(resultSet.getString("room_id")),
                resultSet.getString("name"),
                resultSet.getString("game_status"),
                resultSet.getString("current_turn"),
                resultSet.getString("password"),
                Boolean.parseBoolean(resultSet.getString("is_delete"))
        );
    }

    public List<RoomEntity> findAll(final int page, final int size) {
        final String sql = "SELECT * FROM room WHERE is_delete = ? LIMIT ? OFFSET ?";
        final RowMapper<RoomEntity> rowMapper = createRoomEntityRowMapper();
        return jdbcTemplate.query(sql, rowMapper, false, size, calculatePage(page, size));
    }

    private int calculatePage(final int page, final int size) {
        if (page < 1) {
            return 0;
        }
        return (page - 1) * size;
    }

    public Optional<RoomEntity> findByName(final String roomName) {
        try {
            final String sql = "SELECT * FROM room WHERE name = ? AND is_delete = ?";
            final RowMapper<RoomEntity> rowMapper = createRoomEntityRowMapper();
            final RoomEntity roomEntity = jdbcTemplate.queryForObject(sql, rowMapper, roomName, false);
            return Optional.of(roomEntity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int count() {
        final String sql = "SELECT COUNT(*) FROM room WHERE is_delete = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, false);
    }

    public int save(final RoomEntity roomEntity) {
        final String sql = "INSERT INTO room (name, game_status, current_turn, password) VALUES (?, ?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final PreparedStatementCreator statementCreator = con -> {
            final PreparedStatement ps = con.prepareStatement(sql, new String[]{"room_id"});
            ps.setString(1, roomEntity.getName());
            ps.setString(2, roomEntity.getGameStatus());
            ps.setString(3, roomEntity.getCurrentTurn());
            ps.setString(4, roomEntity.getPassword());
            return ps;
        };
        jdbcTemplate.update(statementCreator, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public int deleteById(final int roomId) {
        final String sql = "UPDATE room SET is_delete = ? WHERE room_id = ?";
        return jdbcTemplate.update(sql, true, roomId);
    }

    public int updateById(final int roomId, final GameStatus gameStatus, final Color currentTurn) {
        final String sql = "UPDATE room SET game_status = ?, current_turn = ? WHERE room_id = ?";
        return jdbcTemplate.update(sql, gameStatus.getValue(), currentTurn.getValue(), roomId);
    }
}
