package chess.dao;

import chess.domain.chesspiece.ChessPiece;
import chess.domain.position.Position;
import chess.dto.ChessPieceMapper;
import chess.entity.ChessPieceEntity;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ChessPieceDao {

    private final JdbcTemplate jdbcTemplate;

    public ChessPieceDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ChessPieceEntity> findAllEntityByRoomId(final int roomId) {
        final String sql = "SELECT * FROM chess_piece WHERE room_id = ?";
        final RowMapper<ChessPieceEntity> rowMapper = (resultSet, rowNum) -> new ChessPieceEntity(
                Integer.parseInt(resultSet.getString("chess_piece_id")),
                roomId,
                resultSet.getString("position"),
                resultSet.getString("chess_piece"),
                resultSet.getString("color"));
        return jdbcTemplate.query(sql, rowMapper, roomId);
    }

    public int deleteByRoomIdAndPosition(final int roomId, final Position position) {
        final String sql = "DELETE FROM chess_piece WHERE room_id = ? AND position = ?";
        return jdbcTemplate.update(sql, roomId, position.getValue());
    }

    @Transactional
    public int saveAll(final int roomId, final Map<Position, ChessPiece> pieceByPosition) {
        String sql = "INSERT INTO chess_piece (room_id, position, chess_piece, color) VALUES (?, ?, ?, ?)";
        final List<Object[]> batchArguments = toBatchArguments(roomId, pieceByPosition);
        final int[] result = jdbcTemplate.batchUpdate(sql, batchArguments);
        return Arrays.stream(result).sum();
    }

    private List<Object[]> toBatchArguments(final int roomId, final Map<Position, ChessPiece> pieceByPosition) {
        return pieceByPosition.entrySet()
                .stream()
                .map(entry -> toBatchArgument(roomId, entry))
                .collect(Collectors.toList());
    }

    private Object[] toBatchArgument(final int roomId, final Entry<Position, ChessPiece> entry) {
        return new Object[]{
                roomId,
                entry.getKey().getValue(),
                ChessPieceMapper.toPieceType(entry.getValue()),
                entry.getValue().color().getValue()
        };
    }

    public int updateByRoomIdAndPosition(final int roomId, final Position from, final Position to) {
        final String sql = "UPDATE chess_piece SET position = ? WHERE room_id = ? AND position = ?";
        return jdbcTemplate.update(sql, to.getValue(), roomId, from.getValue());
    }
}
