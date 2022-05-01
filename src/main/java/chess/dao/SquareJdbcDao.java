package chess.dao;

import chess.entity.SquareEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SquareJdbcDao implements SquareDao {

    private final JdbcTemplate jdbcTemplate;

    public SquareJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveAll(List<SquareEntity> squares, long roomId) {
        String sql = "insert into square (position, piece, room_id) values (?, ?, ?)";

        List<Object[]> result = squares.stream()
                .map(s -> new Object[]{s.getPosition(), s.getPiece(), roomId})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sql, result);
    }

    @Override
    public List<SquareEntity> findByRoomId(long roomId) {
        String sql = "select * from square where room_id = ?";
        return jdbcTemplate.query(sql, rowMapper(), roomId);
    }

    @Override
    public Optional<SquareEntity> findByRoomIdAndPosition(long roomId, String position) {
        String sql = "select * from square where room_id = ? and position = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper(), roomId, position));
    }

    @Override
    public void update(long roomId, String position, String piece) {
        String sql = "update square set piece = ? where room_id = ? and position = ?";
        jdbcTemplate.update(sql, piece, roomId, position);
    }

    @Override
    public void removeAll(long roomId) {
        String sql = "delete from square where room_id = ?";
        jdbcTemplate.update(sql, roomId);
    }

    private RowMapper<SquareEntity> rowMapper() {
        return (result, rowNum) -> new SquareEntity(
                result.getLong("id"),
                result.getLong("room_id"),
                result.getString("position"),
                result.getString("piece")
        );
    }
}
