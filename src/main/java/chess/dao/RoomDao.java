package chess.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import chess.domain.board.BoardDto;
import chess.domain.room.Room;
import chess.repository.RoomRepository;

@Repository
public class RoomDao implements RoomRepository {

    private final JdbcTemplate jdbcTemplate;

    public RoomDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Room> findAll() {
        String sql = "SELECT r.room_id, r.title FROM room r";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            final long roomId = rs.getLong("r.room_id");
            final String title = rs.getString("r.title");
            return new Room(roomId, title);
        });
    }

    @Override
    public long findChessIdById(long roomId) {
        String sql = "SELECT c.chess_id FROM chess c WHERE c.room_id = ?";
        return Objects.requireNonNull(
                jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getLong("chess_id"), roomId));
    }

    @Override
    public long insert(String title, BoardDto boardDto) {
        final long roomId = insertRoom(title);
        final long chessId = insertChess(roomId);
        insertPieces(chessId, boardDto);
        return chessId;
    }

    private long insertRoom(String title) {
        String sql = "INSERT INTO room (title) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement ps =
                    connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    private long insertChess(long roomId) {
        String sql = "INSERT INTO chess (room_id, status, turn) VALUES (?, 'RUNNING', 'WHITE')";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement ps =
                    connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, roomId);
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    private void insertPieces(long chessId, BoardDto boardDto) {
        String sql = "INSERT INTO piece (chess_id, position, color, name) VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(
                sql,
                boardDto.getPieceDtos(),
                boardDto.getPieceDtos().size(),
                (pstmt, argument) -> {
                    pstmt.setLong(1, chessId);
                    pstmt.setString(2, argument.getPosition());
                    pstmt.setString(3, argument.getColor());
                    pstmt.setString(4, argument.getName());
                });
    }
}
