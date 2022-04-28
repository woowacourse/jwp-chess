package chess.dao;

import static java.util.stream.Collectors.toList;

import chess.domain.chesspiece.ChessPiece;
import chess.domain.position.Position;
import chess.entity.ChessPieceEntity;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcChessPieceDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcChessPieceDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveAll(final int roomId, final Map<Position, ChessPiece> piecesByPosition) {
        final String sql = "INSERT INTO chess_piece (room_id, position, chess_piece, color) VALUES (?, ?, ?, ?)";
        final List<Object[]> parameters = generateParameters(roomId, piecesByPosition);
        jdbcTemplate.batchUpdate(sql, parameters);
    }

    private List<Object[]> generateParameters(final int roomId, final Map<Position, ChessPiece> piecesByPosition) {
        return piecesByPosition.entrySet()
                .stream()
                .map(entry -> generateParameter(roomId, entry))
                .collect(toList());
    }

    private Object[] generateParameter(final int roomId, final Entry<Position, ChessPiece> entry) {
        final Position position = entry.getKey();
        final ChessPiece chessPiece = entry.getValue();
        return new Object[]{
                roomId,
                position.getValue(),
                ChessPieceMapper.toPieceType(chessPiece),
                chessPiece.color().getValue()
        };
    }

    public List<ChessPieceEntity> findByRoomId(final int roomId) {
        final String sql = "SELECT * FROM chess_piece WHERE room_id = ?";
        return jdbcTemplate.query(sql, createChessPieceRowMapper(), roomId);
    }

    private RowMapper<ChessPieceEntity> createChessPieceRowMapper() {
        return (rs, rowNum) -> {
            final String chessPieceId = rs.getString("id");
            final String roomId = rs.getString("room_id");
            final String position = rs.getString("position");
            final String chessPiece = rs.getString("chess_piece");
            final String color = rs.getString("color");

            return new ChessPieceEntity(Integer.parseInt(chessPieceId), Integer.parseInt(roomId), position, chessPiece,
                    color);
        };
    }

    public void update(final int roomId, final String from, final String to) {
        final String sql = "UPDATE chess_piece SET position = ? WHERE room_id = ? AND position = ?";
        jdbcTemplate.update(sql, to, roomId, from);
    }

    public void deleteByRoomIdAndPosition(final int roomId, final String to) {
        final String sql = "DELETE FROM chess_piece WHERE room_id = ? AND position  = ?";
        jdbcTemplate.update(sql, roomId, to);
    }

    public void deleteByRoomId(final int roomId) {
        final String sql = "DELETE FROM chess_piece WHERE room_id = ?";
        jdbcTemplate.update(sql, roomId);
    }
}
